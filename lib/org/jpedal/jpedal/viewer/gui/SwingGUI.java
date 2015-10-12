

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
 * SwingGUI.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.AbstractBorder;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.jpedal.*;
import org.jpedal.display.Display;
import org.jpedal.display.GUIDisplay;
import org.jpedal.display.GUIModes;
import org.jpedal.display.PageOffsets;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.RecentDocuments;
import org.jpedal.examples.viewer.RecentDocumentsFactory;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.commands.OpenFile;
import org.jpedal.examples.viewer.gui.generic.GUIButtons;
import org.jpedal.examples.viewer.gui.generic.GUICombo;
import org.jpedal.examples.viewer.gui.generic.GUIMenuItems;
import org.jpedal.examples.viewer.gui.generic.GUIMouseHandler;
import org.jpedal.examples.viewer.gui.generic.GUISearchList;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.popups.PrintPanel;
import org.jpedal.examples.viewer.gui.swing.*;
import org.jpedal.examples.viewer.paper.PaperSizes;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.external.CustomMessageHandler;
import org.jpedal.external.Options;
import org.jpedal.fonts.tt.TTGlyph;
import org.jpedal.gui.*;
import org.jpedal.io.StatusBar;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.*;
import org.w3c.dom.Node;


/**
 * <br>Description: Swing GUI functions in Viewer
 *
 *
 */
@SuppressWarnings("MagicConstant")
public class SwingGUI extends GUI implements GUIFactory {
    
    protected final Buttons swButtons = new Buttons();
    
    //GUICursor object that holds everything todo with Cursor for SwingGUI
    private final SwingCursor guiCursor = new SwingCursor();
    
    @SuppressWarnings("UnusedDeclaration")

    /**all mouse actions*/
    private GUIMouseHandler mouseHandler;

    private Timer memoryMonitor;

    //flag for marks new thumbnail preview
    private boolean debugThumbnail;
    
    private SwingScrollListener scrollListener;
    
    /**
     * This scroll bar allows for page navigation via the scroll bar/wheel when the page
     * fits within the dimensions of the viewable area
     */
    private JScrollBar thumbscroll;

    //Creates the top toolbar
    private final JPanel top;
    
    private boolean sideTabBarOpenByDefault;
    private String startSelectedTab = "";
    

    //use new GUI layout
	private boolean hasListener;
	private boolean isSetup;
	private int lastTabSelected=-1;
	private boolean tabsExpanded;

    private PaperSizes paperSizes;

    /** Multibox for new GUI Layout*/
    //Component to contain memory, cursor and loading bars
    private final JPanel multibox;

    /** Track whether both pages are properly displayed */
    private boolean pageTurnScalingAppropriate =true;

	/**holds back/forward buttons at bottom of page*/
	private JToolBar navButtons;

    /**hold the thumbnails for display panes*/
    private final JPanel containerForThumbnails;
    
	/**tell user on first form change it can be saved*/
	private static final boolean firstTimeFormMessage=true;

	/** visual display of current cursor co-ords on page*/
	private JLabel coords;

	/**root element to hold display*/
	private Container frame;

	/** alternative internal JFrame*/
	private JDesktopPane desktopPane;

	/**flag to disable functions*/
	private boolean isSingle=true;

	/**displayed on left to hold thumbnails, bookmarks*/
	private JTabbedPane navOptionsPanel;

	/**split display between PDF and thumbnails/bookmarks*/
	private JSplitPane displayPane;


	/**Scrollpane for pdf panel*/
	private JScrollPane scrollPane;

	/**Interactive display object - needs to be added to PdfDecoder*/
	private StatusBar statusBar=new StatusBar(new Color(235, 154, 0));
	private StatusBar downloadBar=new StatusBar(new Color(185, 209, 0));

	private JLabel pageCounter1;

    //allow user to control messages in Viewer
    private CustomMessageHandler customMessageHandler;

	public JTextField pageCounter2;

	private JLabel pageCounter3;

	private JLabel optimizationLabel;

	private SwingSignaturesPanel signaturesTree;

	private SwingLayersPanel layersPanel;

    /**stop user forcing open tab before any pages loaded*/
	private boolean tabsNotInitialised=true;
	private JToolBar navToolBar;
	private JToolBar pagesToolBar;


	
	//Progress bar on nav bar
	private final JProgressBar memoryBar;

	//Component to display cursor position on page
	private JToolBar cursor = new JToolBar();
	

	public SwingGUI(final PdfDecoderInt decode_pdf, final Values commonValues, final GUIThumbnailPanel thumbnails, final PropertiesFile properties) {
        super(decode_pdf, commonValues, thumbnails, properties);       
        
        // Make sure this is called before initialising the components
        setLookAndFeel();
        
        /** Initialise the java components */
        top = new JPanel();
        multibox = new JPanel();
        navButtons = new JToolBar();
        containerForThumbnails=new JPanel();
        coords=new JLabel();
        frame=new JFrame();
        desktopPane=new JDesktopPane();
        navOptionsPanel=new JTabbedPane();
        pageCounter2 = new JTextField(4);
        pageCounter3 = new JLabel();
        memoryBar = new JProgressBar();
        cursor = new JToolBar();
        navToolBar = new JToolBar();
        pagesToolBar = new JToolBar();
        signaturesTree = new SwingSignaturesPanel();
        layersPanel=new SwingLayersPanel();
        scrollPane = new JScrollPane();
        tree=new SwingOutline();
        
        setupDisplay();
	}
    
    /**
     * set the look and feel for the GUI components to be the
     * default for the system it is running on
     */ 
    private static void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        }catch (final Exception e) {
            LogWriter.writeLog("Exception " + e + " setting look and feel");
        }
    }
    
   /**
    * setup display
    */
    private void setupDisplay(){
        if (SwingUtilities.isEventDispatchThread()) {

            decode_pdf.setDisplayView(Display.SINGLE_PAGE, Display.DISPLAY_CENTERED);

        } else {
            final Runnable doPaintComponent = new Runnable() {

                @Override
                public void run() {
                    decode_pdf.setDisplayView(Display.SINGLE_PAGE, Display.DISPLAY_CENTERED);
                }
            };
            SwingUtilities.invokeLater(doPaintComponent);
        }

        //pass in SwingGUI so we can call via callback
        decode_pdf.addExternalHandler(this,Options.GUIContainer);

        /**
         * pass properties into Swing Menu items
         */
        menuItems=new SwingMenuItems(properties);

        /**
         * setup display multiview display
         */
        if (isSingle) {
            desktopPane.setBackground(frame.getBackground());
            desktopPane.setVisible(true);
            if(frame instanceof JFrame) {
                ((JFrame) frame).getContentPane().add(desktopPane, BorderLayout.CENTER);
            } else {
                frame.add(desktopPane, BorderLayout.CENTER);
            }

        }
    }
    
    @Override
	public JSplitPane getDisplayPane() {
		return displayPane;
	}

    @Override
    public JScrollBar getThumbnailScrollBar() {
		return thumbscroll;
	}
    
    @Override
    public void setThumbnailScrollBarVisibility(final boolean isVisible) {
        thumbscroll.setVisible(isVisible);
    }
    
    @Override
    public void setThumbnailScrollBarValue(final int pageNum) {
        thumbscroll.setValue(pageNum);
    }
    
    @Override
    public Object getMultiViewerFrames(){
            return desktopPane;
    }

    @Override
    public String getPropertiesFileLocation(){
        return properties.getConfigFile();
    }
    
    @Override
	public String getBookmark(final String bookmark) {
		return tree.getPage(bookmark);
	}
    
    @Override
	public void reinitialiseTabs(final boolean showVisible) {

        //not needed
        if(commonValues.getModeOfOperation()==Values.RUNNING_PLUGIN) {
            return;
        }
        
        if(properties.getValue("ShowSidetabbar").toLowerCase().equals("true")){

			if(!isSingle) {
                return;
            }

			if(!showVisible &&  !properties.getValue("consistentTabBar").toLowerCase().equals("true")){
				if(sideTabBarOpenByDefault){
					displayPane.setDividerLocation(expandedSize);
					tabsExpanded = true;
				}else{
					displayPane.setDividerLocation(collapsedSize);
					tabsExpanded = false;
					navOptionsPanel.setSelectedIndex(-1);
				}
			}
			lastTabSelected=-1;

			if(!commonValues.isPDF()){
				navOptionsPanel.setVisible(false);
			}else{
				navOptionsPanel.setVisible(true);
				
				/**
				 * add/remove optional tabs
				 */
				if(!decode_pdf.hasOutline()){

                    int outlineTab=-1;

					if(DecoderOptions.isRunningOnMac){
						//String tabName="";
						//see if there is an outlines tab
						for(int jj=0;jj<navOptionsPanel.getTabCount();jj++){
							if(navOptionsPanel.getTitleAt(jj).equals(bookmarksTitle)) {
                                outlineTab = jj;
                            }
						}
					}else{
						//String tabName="";
						//see if there is an outlines tab
						for(int jj=0;jj<navOptionsPanel.getTabCount();jj++){
							if(navOptionsPanel.getIconAt(jj).toString().equals(bookmarksTitle)) {
                                outlineTab = jj;
                            }
						}
					}

					if(outlineTab!=-1) {
                        navOptionsPanel.remove(outlineTab);
                    }

				}else if(properties.getValue("Bookmarkstab").toLowerCase().equals("true")){
					int outlineTab=-1;
					if(DecoderOptions.isRunningOnMac){
						//String tabName="";
						//see if there is an outlines tab
						for(int jj=0;jj<navOptionsPanel.getTabCount();jj++){
							if(navOptionsPanel.getTitleAt(jj).equals(bookmarksTitle)) {
                                outlineTab = jj;
                            }
						}

						if(outlineTab==-1) {
                            navOptionsPanel.addTab(bookmarksTitle, (SwingOutline) tree);
                        }
					}else{
						//String tabName="";
						//see if there is an outlines tab
						for(int jj=0;jj<navOptionsPanel.getTabCount();jj++){
							if(navOptionsPanel.getIconAt(jj).toString().equals(bookmarksTitle)) {
                                outlineTab = jj;
                            }
						}

						if(outlineTab==-1){
							final VTextIcon textIcon2 = new VTextIcon(navOptionsPanel, bookmarksTitle, VTextIcon.ROTATE_LEFT);
							navOptionsPanel.addTab(null, textIcon2, (SwingOutline) tree);
						}
					}
				}

				/** handle signatures pane*/
				final AcroRenderer currentFormRenderer = decode_pdf.getFormRenderer();

                Iterator<FormObject> signatureObjects=null;

                if(currentFormRenderer!=null){
                    signatureObjects = currentFormRenderer.getSignatureObjects();
                }

                if(signatureObjects != null){
                    signaturesTree.reinitialise(decode_pdf, signatureObjects);
                    checkTabShown(signaturesTitle);
                }else {
                    removeTab(signaturesTitle);
                }

                //<link><a name="layers" />
				/**
				 * add a control Panel to enable/disable layers
				 */
				//layers object
				layersObject=(PdfLayerList)decode_pdf.getJPedalObject(PdfDictionary.Layer);

                if(layersObject != null && layersObject.getLayersCount()>0){ //some files have empty Layers objects

					checkTabShown(layersTitle);
                    layersPanel.reinitialise(layersObject, decode_pdf, scrollPane, commonValues.getCurrentPage());

				} else {
                    removeTab(layersTitle);
                }

                setBookmarks(false);
			}
			
			if(tabsNotInitialised){
				navOptionsPanel.setSelectedIndex(-1);
				for(int i=0; i!=navOptionsPanel.getTabCount(); i++){
					if(DecoderOptions.isRunningOnMac){
						if(navOptionsPanel.getTitleAt(i).equals(startSelectedTab)){
							navOptionsPanel.setSelectedIndex(i);
							break;
						}
					}else{
						if(navOptionsPanel.getIconAt(i).toString().equals(startSelectedTab)){
							navOptionsPanel.setSelectedIndex(i);
							break;
						}
					}
				}
			}
		}
	}

	private void checkTabShown(final String title) {
		int outlineTab=-1;
		if(DecoderOptions.isRunningOnMac){

			//see if there is an outlines tab
			for(int jj=0;jj<navOptionsPanel.getTabCount();jj++){
				if(navOptionsPanel.getTitleAt(jj).equals(title)) {
                    outlineTab = jj;
                }
			}

			if(outlineTab==-1){
				if(title.equals(signaturesTitle) && properties.getValue("Signaturestab").toLowerCase().equals("true")){
					navOptionsPanel.addTab(signaturesTitle, signaturesTree);
					navOptionsPanel.setTitleAt(navOptionsPanel.getTabCount()-1, signaturesTitle);

				}else if(title.equals(layersTitle) && properties.getValue("Layerstab").toLowerCase().equals("true")){

					final JScrollPane scrollPane=new JScrollPane();
					scrollPane.getViewport().add(layersPanel);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

					navOptionsPanel.addTab(layersTitle, scrollPane);
					navOptionsPanel.setTitleAt(navOptionsPanel.getTabCount()-1, layersTitle);

				}
			}

		}else{
			//see if there is an outlines tab
			for(int jj=0;jj<navOptionsPanel.getTabCount();jj++){
				if(navOptionsPanel.getIconAt(jj).toString().equals(title)) {
                    outlineTab = jj;
                }
			}

			if(outlineTab==-1){

                if(title.equals(signaturesTitle) && properties.getValue("Signaturestab").toLowerCase().equals("true")){  //stop spurious display of Sig tab
				    final VTextIcon textIcon2 = new VTextIcon(navOptionsPanel, signaturesTitle, VTextIcon.ROTATE_LEFT);
				    navOptionsPanel.addTab(null, textIcon2, signaturesTree);
//				    navOptionsPanel.setTitleAt(navOptionsPanel.getTabCount()-1, signaturesTitle);
                }else if(title.equals(layersTitle) && properties.getValue("Layerstab").toLowerCase().equals("true")){
                    final VTextIcon textIcon = new VTextIcon(navOptionsPanel, layersTitle, VTextIcon.ROTATE_LEFT);

                    final JScrollPane scrollPane=new JScrollPane();
                    scrollPane.getViewport().add(layersPanel);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                    navOptionsPanel.addTab(null, textIcon, scrollPane);
                }
				//navOptionsPanel.setTitleAt(navOptionsPanel.getTabCount()-1, layersTitle);
			}
		}
	}

	private void removeTab(final String title) {


		int outlineTab=-1;

		if(DecoderOptions.isRunningOnMac){
			//String tabName="";
			//see if there is an outlines tab
			for(int jj=0;jj<navOptionsPanel.getTabCount();jj++){
				if(navOptionsPanel.getTitleAt(jj).equals(title)) {
                    outlineTab = jj;
                }
			}
		}else{
			//String tabName="";
			//see if there is an outlines tab
			for(int jj=0;jj<navOptionsPanel.getTabCount();jj++){
				if(navOptionsPanel.getIconAt(jj).toString().equals(title)) {
                    outlineTab = jj;
                }
			}
		}

		if(outlineTab!=-1) {
            navOptionsPanel.remove(outlineTab);
        }

	}

    @Override
	public void stopThumbnails() {

		if(!isSingle) {
            return;
        }

		if(thumbnails.isShownOnscreen()){
			/** if running terminate first */
			thumbnails.terminateDrawing();
            
			thumbnails.removeAllListeners();

		}
	}

    @Override
	public void reinitThumbnails() {

		isSetup=false;

	}

	/**reset so appears closed*/
	@Override
    public void resetNavBar() {

			if(!isSingle) {
                return;
            }
            if(!properties.getValue("consistentTabBar")
                    .toLowerCase().equals("true")){
                displayPane.setDividerLocation(collapsedSize);
    			navOptionsPanel.setSelectedIndex(-1);
                tabsNotInitialised=true;
			}
			
			//also reset layers
            layersPanel.resetLayers();

			//disable page view buttons until we know we have multiple pages
			swButtons.setPageLayoutButtonsEnabled(false);
		
		}

    /* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#setNoPagesDecoded()
	 *
	 * Called when new file opened so we set flags here
	 */
	@Override
    public void setNoPagesDecoded() {


        bookmarksGenerated=false;

        resetNavBar();

        //Ensure preview from last file doesn't appear
        if(scrollListener!=null) {
            scrollListener.lastImage = null;
        }

	}

    @Override
	public void setDisplayMode(final Integer mode) {

		if(mode.equals(GUIFactory.MULTIPAGE)) {
            isSingle = false;
        }

	}

    @Override
	public boolean isSingle() {
		return isSingle;
	}

    @Override
    public Object getThumbnailPanel() {
        return thumbnails;
    }

    @Override
    public Object getOutlinePanel() {
        return tree;  
    }
    
    @Override
    public Object getVerticalScrollBar() {
    	if(scrollPane.getVerticalScrollBar().isVisible()){
    		return scrollPane.getVerticalScrollBar();
    	}else {
            return thumbscroll;
        }
	}
    
    @Override
    public void setScrollBarPolicy(final ScrollPolicy pol) {
        switch (pol) {
            case VERTICAL_NEVER:
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                break;
            case VERTICAL_AS_NEEDED:
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                break;
            case HORIZONTAL_NEVER:
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                break;
            case HORIZONTAL_AS_NEEDED:
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                break;

        }
    }
    
    @Override
    public void setRootContainer(final Object rawValue){
        
        if(rawValue==null) {
            throw new RuntimeException("Null containers not allowed.");
        }

        final Container rootContainer=(Container) rawValue;
        
        Container c = rootContainer;

        if((rootContainer instanceof JTabbedPane)){
            final JPanel temp = new JPanel(new BorderLayout());
            rootContainer.add(temp);
            c = temp;
        }else if(rootContainer instanceof JScrollPane){
            final JPanel temp = new JPanel(new BorderLayout());
            ((JScrollPane)rootContainer).getViewport().add(temp);
            c = temp;

        }else if(rootContainer instanceof JSplitPane){
            throw new RuntimeException("To add the viewer to a split pane please pass through either JSplitPane.getLeftComponent() or JSplitPane.getRightComponent()");
        }

        if(!(rootContainer instanceof JFrame)){
            c.setLayout(new BorderLayout());
        }

        //Load width and height from properties file
        int width = Integer.parseInt(properties.getValue("startViewerWidth"));
        int height = Integer.parseInt(properties.getValue("startViewerHeight"));
        
        //Used to prevent infinite scroll issue as a preferred size has been set
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        if(width<0){
        	width = d.width / 2;
            if(width<700) {
                width = 700;
            }
            properties.setValue("startViewerWidth", String.valueOf(width));
        }
        
        if(height<0){
        	height = d.height / 2;
            properties.setValue("startViewerHeight", String.valueOf(height));
        }
        
        //allow user to alter size
        final String customWindowSize=System.getProperty("org.jpedal.startWindowSize");
        if(customWindowSize!=null){

            final StringTokenizer values=new StringTokenizer(customWindowSize,"x");

            System.out.println(values.countTokens());
            if(values.countTokens()!=2) {
                throw new RuntimeException("Unable to use value for org.jpedal.startWindowSize=" + customWindowSize + "\nValue should be in format org.jpedal.startWindowSize=200x300");
            }

            try{
                width=Integer.parseInt(values.nextToken().trim());
                height=Integer.parseInt(values.nextToken().trim());

            }catch(final Exception ee){
                throw new RuntimeException("Unable to use value for org.jpedal.startWindowSize="+customWindowSize+"\nValue should be in format org.jpedal.startWindowSize=200x300 "+ee);
            }
        }

        c.setPreferredSize(new Dimension(width, height));

        frame = c;
    }

    /* (non-Javadoc)
     * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#resetRotationBox()
     */
    @Override
    public void resetRotationBox() {

        final PdfPageData currentPageData = decode_pdf.getPdfPageData();

        //>>> DON'T UNCOMMENT THIS LINE, causes major rotation issues, only useful for debuging <<<
        if (decode_pdf.getDisplayView() == Display.SINGLE_PAGE) {
            rotation = currentPageData.getRotation(commonValues.getCurrentPage());
        }
        //else
        //rotation=0;

        if (getSelectedComboIndex(Commands.ROTATION) != (rotation / 90)) {
            setSelectedComboIndex(Commands.ROTATION, (rotation / 90));
        } else if (!Values.isProcessing()) {
            //<start-fx>
            ((PdfDecoder)decode_pdf).repaint();
            //<end-fx>
        }
    }
    
	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#getProperties()
	 */
    @Override
    public PropertiesFile getProperties() {
        return properties;
    }

	/**
	 * display form data in popup
	 *
	private class ShowFormDataListener implements ActionListener{

		private String formName;

		public ShowFormDataListener(String formName){
			this.formName=formName;
		}

		public void actionPerformed(ActionEvent e) {


			//will return Object or  Object[] if multiple items of same name
			Object[] values=decode_pdf.getFormRenderer().getCompData().getRawForm(formName);

			int count=values.length;

			JTabbedPane valueDisplay=new JTabbedPane();
			for(int jj=0;jj<count;jj++){

                FormObject form=(FormObject)values[jj];

				if(values[jj]!=null){
					String data=form.toString();
					JTextArea text=new JTextArea();
					text.setText(data);
					text.setWrapStyleWord(true);

					JScrollPane scroll=new JScrollPane();
					scroll.setPreferredSize(new Dimension(400,300));
					scroll.getViewport().add(text);
					scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

					valueDisplay.add(form.getObjectRefAsString(),scroll);
				}
			}

			JOptionPane.showMessageDialog(getFrame(), valueDisplay,"Raw Form Data",JOptionPane.OK_OPTION);
		}

	}/**/

	private boolean searchInMenu;

	/*
	 * Set Search Bar to be in the Left hand Tabbed pane
	 */
    @Override
	public void searchInTab(final GUISearchWindow searchFrame){
		this.searchFrame = searchFrame;
		
		this.searchFrame.init(decode_pdf, commonValues);
		
		if(DecoderOptions.isRunningOnMac){
			if(thumbnails.isShownOnscreen()) {
                navOptionsPanel.addTab("Search", ((SwingSearchWindow) searchFrame).getContentPane());
            }
		}else{
			final VTextIcon textIcon2 = new VTextIcon(navOptionsPanel, "Search", VTextIcon.ROTATE_LEFT);
			navOptionsPanel.addTab(null, textIcon2, ((SwingSearchWindow)searchFrame).getContentPane());
		}
	}
	private JTextField searchText;
	private GUISearchList results;
	
	private JToggleButton options;
	private JPopupMenu menu;
	
	private JToggleButton createMenuBarSearchOptions(){
		if(options==null){
			options = new JToggleButton(new ImageIcon(guiCursor.getURLForImage("menuSearchOptions.png")));
			menu = new JPopupMenu();

			options.addItemListener(new ItemListener() {
				@Override
                public void itemStateChanged(final ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						menu.show(((JComponent)e.getSource()), 0, ((JComponent)e.getSource()).getHeight());
					}
				}
			});
			options.setFocusable(false);
			options.setToolTipText(Messages.getMessage("PdfViewerSearch.Options"));
			//wholeWordsOnlyBox, caseSensitiveBox, multiLineBox, highlightAll

			//		JMenuItem openFull = new JMenuItem("Open Full Search Window");
			//		openFull.addActionListener(new ActionListener(){
			//			public void actionPerformed(ActionEvent e) {
			//				
			//			}
			//		});

			final JCheckBoxMenuItem wholeWords = new JCheckBoxMenuItem(Messages.getMessage("PdfViewerSearch.WholeWords"));
			wholeWords.addActionListener(new ActionListener(){
				@Override
                public void actionPerformed(final ActionEvent e) {
					searchFrame.setWholeWords(((JCheckBoxMenuItem)e.getSource()).isSelected());
					enableSearchItems(true);
				}
			});

			final JCheckBoxMenuItem caseSense = new JCheckBoxMenuItem(Messages.getMessage("PdfViewerSearch.CaseSense"));
			caseSense.addActionListener(new ActionListener(){
				@Override
                public void actionPerformed(final ActionEvent e) {
					searchFrame.setCaseSensitive(((JCheckBoxMenuItem)e.getSource()).isSelected());
					enableSearchItems(true);
				}
			});

			final JCheckBoxMenuItem multiLine = new JCheckBoxMenuItem(Messages.getMessage("PdfViewerSearch.MultiLine"));
			multiLine.addActionListener(new ActionListener(){
				@Override
                public void actionPerformed(final ActionEvent e) {
					searchFrame.setMultiLine(((JCheckBoxMenuItem)e.getSource()).isSelected());
					enableSearchItems(true);
				}
			});

			//        menu.add(openFull);
			//        menu.addSeparator();
			menu.add(wholeWords);
			menu.add(caseSense);
			menu.add(multiLine);

			menu.addPopupMenuListener(new PopupMenuListener() {
				@Override
                public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
				}

				@Override
                public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
					options.setSelected(false);
				}

				@Override
                public void popupMenuCanceled(final PopupMenuEvent e) {
					options.setSelected(false);
				}
			});
		}

        return options;
	}
	
	/*
	 * Set Search Bar to be in the Top Button Bar
	 */
	private void searchInMenu(final GUISearchWindow searchFrame){
		this.searchFrame = searchFrame;
		searchInMenu = true;
		searchFrame.find(decode_pdf, commonValues);
		swButtons.getTopButtons().add(searchText);
		swButtons.getTopButtons().add(createMenuBarSearchOptions());
		swButtons.addButton(GUIFactory.BUTTONBAR, Messages.getMessage("PdfViewerSearch.Previous"), "search_previous.gif", Commands.PREVIOUSRESULT, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);
		swButtons.addButton(GUIFactory.BUTTONBAR, Messages.getMessage("PdfViewerSearch.Next"), "search_next.gif", Commands.NEXTRESULT, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

		swButtons.getButton(Commands.NEXTRESULT).setEnabled(false);
		swButtons.getButton(Commands.PREVIOUSRESULT).setEnabled(false);
		
		swButtons.getButton(Commands.NEXTRESULT).setVisible(true);
		swButtons.getButton(Commands.PREVIOUSRESULT).setVisible(true);
	}

    @Override
    public void setPropertiesFileLocation(final String file){
        properties.loadProperties(file);
    }
    
    @Override
    public Commands getCommand(){
        return currentCommands;
    }
	
    /* (non-Javadoc)
     * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#init(java.lang.String[], org.jpedal.examples.viewer.Commands, org.jpedal.examples.viewer.utils.Printer)
     */
    @Override
    public void init(final Commands currentCommands, final Object currentPrinter) {

        mouseHandler = new SwingMouseListener((PdfDecoder) decode_pdf, this, commonValues, currentCommands);
        mouseHandler.setupMouse();
        
        final long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK;
        
        //Add universal listener to catch coords updating regardless of form component
        Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener()
        {
        	//Required to prevent recursive calling of listener
            boolean listenerCalled;
            
            @Override
            public void eventDispatched(final AWTEvent e)
            {
            	
            	//ID 503 == Mouse Moved event (506 == Mouse Dragged)
            	if(!listenerCalled && e.getID()==503){
            		listenerCalled = true;

            		final Point mousePosition = ((PdfDecoder)decode_pdf).getMousePosition();

            		if(mousePosition!=null){
            			((SwingMouseListener)mouseHandler).allowMouseCoordsUpdate();
            			((PdfDecoder)decode_pdf).dispatchEvent(new MouseEvent(((PdfDecoder)decode_pdf),
            					e.getID(),
            					System.currentTimeMillis(),
            					0,
            					mousePosition.x,
            					mousePosition.y,
            					0,
            					false));
            		}

            		listenerCalled = false;
            	}
            }
        }, eventMask);
    	
        super.init(currentCommands);

        /**
         * Set up from properties.
         */
        try {
            //Set whether to use hinting
            propValue = properties.getValue("useHinting");
            propValue2 = System.getProperty("org.jpedal.useTTFontHinting");

            //check JVM flag first
            if (propValue2 != null) {
                //check if properties file conflicts
                if (!propValue.isEmpty() && !propValue2.toLowerCase().equals(propValue.toLowerCase())) {
                    JOptionPane.showMessageDialog(null, Messages.getMessage("PdfCustomGui.hintingFlagFileConflict"));
                }

                TTGlyph.useHinting = propValue2.toLowerCase().equals("true");

                //check properties file
            } else {
                TTGlyph.useHinting = !propValue.isEmpty() && propValue.toLowerCase().equals("true");
            }

            //Set icon location
            propValue = properties.getValue("iconLocation");
            if (!propValue.isEmpty()) {
                guiCursor.setIconLocation(propValue);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }

        /**
         * Add Background color to the panel to help break up view.
         */
        setupCenterPanelBackground();

        /**
         * setup combo boxes.
         */
        setupComboBoxes();


        /**
         * add the pdf display to show page.
         */
        setupPDFDisplayPane();

        /**
         * add the pdf display left and right panes.
         */
        setupBorderPanes();

        /**
         * Initialise the Swing Buttons *
         */
        swButtons.init(isSingle);

        /**
         * create a menu bar and add to display
         */
        createTopMenuBar();


        /**
         * set colours on display boxes and add listener to page number
         */
        setupBottomToolBarItems();

        /**
         * create other tool bars and add to display
         */
        createOtherToolBars();

        /**
         * Menu bar for using the majority of functions
         */
        menuItems.createMainMenu(true, currentCommandListener, isSingle, commonValues, currentCommands, swButtons);

        //createSwingMenu(true);
        /**
         * sets up all the toolbar items
         */
	swButtons.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.openFile"),"open.gif",Commands.OPENFILE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);
        
        
		swButtons.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.print"),"print.gif",Commands.PRINT, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);
        

		if(searchFrame!=null && (searchFrame.getViewStyle()==GUISearchWindow.SEARCH_EXTERNAL_WINDOW || (searchFrame.getViewStyle()==GUISearchWindow.SEARCH_MENU_BAR && !isSingle))){
			searchFrame.setViewStyle(GUISearchWindow.SEARCH_EXTERNAL_WINDOW);	
			swButtons.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.search"),"find.gif",Commands.FIND, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);
		}

        	swButtons.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.properties"),"properties.gif",Commands.DOCINFO, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);
        
        if (commonValues.getModeOfOperation()==Values.RUNNING_PLUGIN) {
            swButtons.addButton(GUIFactory.BUTTONBAR, Messages.getMessage("PdfViewerToolbarTooltip.about"), "about.gif", Commands.ABOUT, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);
        }

        	/**snapshot screen function*/
		swButtons.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.snapshot"),"snapshot.gif",Commands.SNAPSHOT, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);
 
        final JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(5,32));
        swButtons.getTopButtons().add(sep);
		
		/**
		 * combo boxes on toolbar
		 * */
		addCombo(Messages.getMessage("PdfViewerToolbarScaling.text"), Messages.getMessage("PdfViewerToolbarTooltip.zoomin"), Commands.SCALING);


		addCombo(Messages.getMessage("PdfViewerToolbarRotation.text"), Messages.getMessage("PdfViewerToolbarTooltip.rotation"), Commands.ROTATION);


        	/**image quality option - allow user to choose between images downsampled
		 * (low memory usage 72 dpi) image hires (high memory usage no downsampling)*/
            addCombo(Messages.getMessage("PdfViewerToolbarImageOp.text"),Messages.getMessage("PdfViewerToolbarTooltip.imageOp"),Commands.QUALITY);

		swButtons.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.mouseMode"),"mouse_select.png",Commands.MOUSEMODE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

        swButtons.getTopButtons().add(sep);
        

        /**
		 * navigation toolbar for moving between pages
		 */
		createNavbar();
        
        /**add cursor location*/
        initCoordBox();

        
//		p.setButtonDefaults(defaultValues);

		//<link><a name="newbutton" />
		/**
		 * external/itext button option example adding new option to Export menu
		 * an icon is set wtih location on classpath
		 * "/org/jpedal/examples/viewer/res/newfunction.gif"
		 * Make sure it exists at location and is copied into jar if recompiled
		 */
		//currentGUI.addButton(currentGUI.BUTTONBAR,tooltip,"/org/jpedal/examples/viewer/res/newfunction.gif",Commands.NEWFUNCTION);

		/**
		 * external/itext menu option example adding new option to Export menu
		 * Tooltip text can be externalised in Messages.getMessage("PdfViewerTooltip.NEWFUNCTION")
		 * and text added into files in res package
		 */


		if(searchFrame!=null && searchFrame.getViewStyle()==GUISearchWindow.SEARCH_MENU_BAR && isSingle) {
            searchInMenu(searchFrame);
        }

		/**status object on toolbar showing 0 -100 % completion */
		initStatus();

//		p.setDisplayDefaults(defaultValues);

		//Ensure all gui sections are displayed correctly
		//Issues found when removing some sections
        frame.invalidate();
        frame.validate();
        frame.repaint();

        /**
		 * Load properties
		 */
		try{
            Properties.load(properties, this, commonValues, swButtons, menuItems);
        }catch(final Exception e){
            e.printStackTrace();
        }
        
         //<start-demo>
        /**
        //<end-demo>

        /**/
        
		/**
		 * set display to occupy half screen size and display, add listener and
		 * make sure appears in centre
		 */
		if(commonValues.getModeOfOperation()!=Values.RUNNING_APPLET){
			
			//Load width and height from properties file
	        int width = Integer.parseInt(properties.getValue("startViewerWidth"));
	        int height = Integer.parseInt(properties.getValue("startViewerHeight"));
	        
	        //Used to prevent infinite scroll issue as a preferred size has been set
	        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	        if(width<0){
	        	width = d.width / 2;
	            if(width<minimumScreenWidth) {
                    width = minimumScreenWidth;
                }
	            properties.setValue("startViewerWidth", String.valueOf(width));
	        }
	        
	        if(height<0){
	        	height = d.height / 2;
	            properties.setValue("startViewerHeight", String.valueOf(height));
	        }
	        
			//allow user to alter size
			final String customWindowSize=System.getProperty("org.jpedal.startWindowSize");
			if(customWindowSize!=null){

				final StringTokenizer values=new StringTokenizer(customWindowSize,"x");

				System.out.println(values.countTokens());
				if(values.countTokens()!=2) {
                    throw new RuntimeException("Unable to use value for org.jpedal.startWindowSize=" + customWindowSize + "\nValue should be in format org.jpedal.startWindowSize=200x300");
                }

				try{
					width=Integer.parseInt(values.nextToken().trim());
					height=Integer.parseInt(values.nextToken().trim());

				}catch(final Exception ee){
					throw new RuntimeException("Unable to use value for org.jpedal.startWindowSize="+customWindowSize+"\nValue should be in format org.jpedal.startWindowSize=200x300 "+ee);
				}
			}
			
            createMainViewerWindow(width, height);
		}

        redrawDocumentOnResize();
        
        setupPDFBorder();
        
        //Set side tab bar state at start up
        if(sideTabBarOpenByDefault){
			displayPane.setDividerLocation(expandedSize);
			tabsExpanded = true;
		}else{
			displayPane.setDividerLocation(collapsedSize);
			tabsExpanded = false;
			navOptionsPanel.setSelectedIndex(-1);
	}

	}

	
     /**
     * method being called from within init to create other tool bars to add to
     * display
     */ 
    @Override
    protected void createOtherToolBars() {
        
        /**
         * setup top main menu toolbar and create options
         */
        top.add(((SwingMenuItems)menuItems).getCurrentMenu(), BorderLayout.NORTH);
        
        /**
         * This is where we add the Buttons for the top ToolBar
         */
        top.add(swButtons.getTopButtons(), BorderLayout.CENTER);
        

        /**
         * This is where we add the Buttons for the bottom ToolBar
         */
        if (frame instanceof JFrame) {
            ((JFrame) frame).getContentPane().add(navButtons, BorderLayout.SOUTH);
        } else {
            frame.add(navButtons, BorderLayout.SOUTH);
        }

        /**
         * This is where we add the display pane to the center of the viewer
         */
        if (displayPane != null) { //null in MultiViewer
            if (frame instanceof JFrame) {
                ((JFrame) frame).getContentPane().add(displayPane, BorderLayout.CENTER);
            } else {
                frame.add(displayPane, BorderLayout.CENTER);
            }

        }

    }
    
	private void handleTabbedPanes() {

		if(tabsNotInitialised) {
            return;
        }

		/**
		 * expand size if not already at size
		 */
		//int currentSize=displayPane.getDividerLocation();
		final int tabSelected=navOptionsPanel.getSelectedIndex();

		if(tabSelected==-1) {
            return;
        }
		
		if(!tabsExpanded){
			/**
			 * workout selected tab
			 */
			//String tabName="";
//			if(PdfDecoder.isRunningOnMac){
//				tabName=navOptionsPanel.getTitleAt(tabSelected);
//			}else
//				tabName=navOptionsPanel.getIconAt(tabSelected).toString();
//
//			if(tabName.equals(pageTitle)){
//				thumbnails.setIsDisplayedOnscreen(true);
//			}
//			else if(tabName.equals(bookmarksTitle)){
//				setBookmarks(true);
//			}

			//if(searchFrame!=null)
			//searchFrame.find();

			/**
			 * workout selected tab
			 */
			final String tabName;
			if(DecoderOptions.isRunningOnMac){
				tabName=navOptionsPanel.getTitleAt(tabSelected);
			}else {
                tabName = navOptionsPanel.getIconAt(tabSelected).toString();
            }

			if(tabName.equals(pageTitle)){
				thumbnails.setIsDisplayedOnscreen(true);
			}else {
                thumbnails.setIsDisplayedOnscreen(false);
            }
			
			displayPane.setDividerLocation(expandedSize);
			tabsExpanded = true;
		}else if(lastTabSelected==tabSelected){
			displayPane.setDividerLocation(collapsedSize);
			tabsExpanded = false;
			thumbnails.setIsDisplayedOnscreen(false);
			navOptionsPanel.setSelectedIndex(-1);
		}
		lastTabSelected=tabSelected;
	}

    private boolean cursorOverPage;
    
    
    @Override
    public void setMultibox(final int[] flags) {



        //deal with flags
        if (flags.length > 1 && flags[0] == CURSOR) {
            //if no change, return
            if (cursorOverPage != (flags[1]==1)) {
                cursorOverPage = flags[1] == 1;
            } else {
                return;
            }
        }

        //LOAD_PROGRESS:
        if (statusBar.isEnabled() && statusBar.isVisible() && !statusBar.isDone()) {
            multibox.removeAll();
            statusBar.getStatusObject().setSize(multibox.getSize());
            multibox.add(statusBar.getStatusObject(), BorderLayout.CENTER);

            multibox.repaint();
            return;
        }

        //CURSOR:
        if (cursor.isEnabled() && cursor.isVisible() && cursorOverPage && decode_pdf.isOpen()) {
            multibox.removeAll();
            multibox.add(coords, BorderLayout.CENTER);

            multibox.repaint();
            return;
        }

        //DOWNLOAD_PROGRESS:
        if (downloadBar.isEnabled() && downloadBar.isVisible() && !downloadBar.isDone() && (decode_pdf.isLoadingLinearizedPDF() || !decode_pdf.isOpen())) {
            multibox.removeAll();
            downloadBar.getStatusObject().setSize(multibox.getSize());
            multibox.add(downloadBar.getStatusObject(), BorderLayout.CENTER);

            multibox.repaint();
            return;
        }

        //MEMORY:
        if (memoryBar.isEnabled() && memoryBar.isVisible()) {
            multibox.removeAll();
            memoryBar.setSize(multibox.getSize());
            memoryBar.setForeground(new Color(125, 145, 255));
            multibox.add(memoryBar, BorderLayout.CENTER);

            multibox.repaint();
        }

    }

    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void setTitle(final String title){
        if(frame instanceof JFrame) {
            ((JFrame) frame).setTitle(title);
        }
    }

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#resetComboBoxes(boolean)
	 */
	@Override
    public void resetComboBoxes(final boolean value) {
		
		if(properties.getValue("Imageopdisplay").toLowerCase().equals("true")) {
            qualityBox.setEnabled(value);
        }
		
        scalingBox.setEnabled(value);
		rotationBox.setEnabled(value);

	}
		
	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#getCombo(int)
	 */
    @Override
    public GUICombo getCombo(final int ID) {

        switch (ID) {

            case Commands.QUALITY:
                return qualityBox;
            case Commands.SCALING:
                return scalingBox;
            case Commands.ROTATION:
                return rotationBox;

        }

        return null;

    }

	/** all scaling and rotation should go through this. */
	@Override
    public void scaleAndRotate(){

        if (decode_pdf.getDisplayView() == Display.PAGEFLOW) {
            decode_pdf.setPageParameters(scaling, commonValues.getCurrentPage(),rotation);
            return;
        }

        //ignore if called too early
        if(!decode_pdf.isOpen() && OpenFile.isPDf) {
            return;
        }

		float width,height;

		// [AWI]: Get the default width of scrollbars from the UI Manager
		int scrollbarWidth = 0;
		final Object scrollbarWidthObj = UIManager.get( "ScrollBar.width" );
		if ( scrollbarWidthObj instanceof Integer ) {
			scrollbarWidth = ((Integer)scrollbarWidthObj).intValue();
			if ( scrollbarWidth < 0 ) {
				scrollbarWidth = 0;
			}
		}
		
		if(isSingle){

			// [AWI]: Get the values of all of the border insets around the scrollpane
			int borderInsetLeft = 0;
			int borderInsetRight = 0;
			int borderInsetTop = 0;
			int borderInsetBottom = 0;
			final Insets borderInsets = scrollPane.getBorder().getBorderInsets( scrollPane );
			if (borderInsets != null) {
				borderInsetLeft = borderInsets.left;
				borderInsetRight = borderInsets.right;
				borderInsetTop = borderInsets.top;
				borderInsetBottom = borderInsets.bottom;
			}

			width = scrollPane.getWidth() - inset - inset - borderInsetLeft - borderInsetRight;
			height = scrollPane.getHeight() - inset - inset - borderInsetTop - borderInsetBottom;

		}else{
			width=desktopPane.getSelectedFrame().getWidth();
			height=desktopPane.getSelectedFrame().getHeight();
		}

		if(decode_pdf!=null){

			//get current location and factor out scaling so we can put back at same page
			//final float x= (decode_pdf.getVisibleRect().x/scaling);
			//final float y= (decode_pdf.getVisibleRect().y/scaling);

			//System.out.println(x+" "+y+" "+scaling+" "+decode_pdf.getVisibleRect());
			/** update value and GUI */
			int index=getSelectedComboIndex(Commands.SCALING);

			if(decode_pdf.getDisplayView()==Display.PAGEFLOW){
				
				//Ensure we only display in window mode
				setSelectedComboIndex(Commands.SCALING, 0);
				index = 0;
				
				//Disable scaling option
				scalingBox.setEnabled(false);
			}else if(decode_pdf.getDisplayView()!=Display.PAGEFLOW){

				//No long pageFlow. enable scaling option
				scalingBox.setEnabled(true);
			}

			if(index==-1){
				String numberValue=(String)getSelectedComboItem(Commands.SCALING);
				float zoom=-1;
				if((numberValue!=null)&&(!numberValue.isEmpty())){
					try{
						zoom= Float.parseFloat(numberValue);
					}catch(final Exception e){

                        if(LogWriter.isOutput()) {
                            LogWriter.writeLog("Exception in getting zoom "+e);
                        }
						zoom=-1;
						//its got characters in it so get first valid number string
						final int length=numberValue.length();
						int ii=0;
						while(ii<length){
							final char c=numberValue.charAt(ii);
							if((c>='0' && c<='9')||c=='.') {
                                ii++;
                            } else {
                                break;
                            }
						}

						if(ii>0) {
                            numberValue = numberValue.substring(0, ii);
                        }

						//try again if we reset above
						if(zoom==-1){
							try{
								zoom= Float.parseFloat(numberValue);
							}catch(final Exception e1){
                                if(LogWriter.isOutput()) {
                                    LogWriter.writeLog("Exception in getting zoom "+e1);
                                }
                                zoom=-1;
                            }
						}
					}
					if(zoom>1000){
						zoom=1000;
					}
				}

				//if nothing off either attempt, use window value
				if(zoom==-1){
					//its not set so use To window value
					index=defaultSelection;
					setSelectedComboIndex(Commands.SCALING, index);
                }else{
                    scaling=decode_pdf.getDPIFactory().adjustScaling(zoom/100);

                    setSelectedComboItem(Commands.SCALING, String.valueOf(zoom));
                }
            }
			
			int page = commonValues.getCurrentPage();
			
			//Multipage tiff should be treated as a single page
			if(commonValues.isMultiTiff()) {
                page = 1;
            }
            
			//always check in facing mode with turnover on
            if (index != -1 || decode_pdf.getDisplayView() == Display.SINGLE_PAGE  || (decode_pdf.getDisplayView() == Display.FACING && decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON))) {
                final PdfPageData pageData = decode_pdf.getPdfPageData();
                int cw,ch,raw_rotation=0;

                if (decode_pdf.getDisplayView()==Display.FACING) {
                    raw_rotation = pageData.getRotation(page);
                }

                final boolean isRotated = (rotation+raw_rotation)%180==90;

                final PageOffsets offsets = (PageOffsets)decode_pdf.getExternalHandler(Options.CurrentOffset);
                switch(decode_pdf.getDisplayView()) {
                    case Display.CONTINUOUS_FACING:
                        if (isRotated) {
                            cw = offsets.getMaxH()*2;
                            ch = offsets.getMaxW();
                        }else{
                            cw = offsets.getMaxW()*2;
                            ch = offsets.getMaxH();
                        }
                        break;
                    case Display.CONTINUOUS:
                        if (isRotated) {
                            cw = offsets.getMaxH();
                            ch = offsets.getMaxW();
                        }else{
                            cw = offsets.getMaxW();
                            ch = offsets.getMaxH();
                        }
                        break;
                    case Display.FACING:
                        int leftPage;

                        if(decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) {
                            leftPage = (page/2)*2;
                            if (commonValues.getPageCount() == 2) {
                                leftPage = 1;
                            }
                        } else {
                            leftPage = (page);
                            if ((leftPage & 1) == 0) {
                                leftPage--;
                            }
                        }
                        
                        if(leftPage==0) {
                            leftPage++;
                        }
                        
                        if (isRotated) {
                            cw = pageData.getCropBoxHeight(leftPage);

                            //if first or last page double the width, otherwise add other page width
                            if (leftPage+1 > commonValues.getPageCount() || leftPage == 1) {
                                cw *= 2;
                            } else {
                                cw += pageData.getCropBoxHeight(leftPage + 1);
                            }

                            ch = pageData.getCropBoxWidth(leftPage);
                            if (leftPage+1 <= commonValues.getPageCount() && ch < pageData.getCropBoxWidth(leftPage+1)) {
                                ch = pageData.getCropBoxWidth(leftPage + 1);
                            }
                        }else{
                            cw = pageData.getCropBoxWidth(leftPage);

                            //if first or last page double the width, otherwise add other page width
                            if (leftPage+1 > commonValues.getPageCount()) {
                                cw *= 2;
                            } else {
                                cw += pageData.getCropBoxWidth(leftPage + 1);
                            }

                            ch = pageData.getCropBoxHeight(leftPage);
                            if (leftPage+1 <= commonValues.getPageCount() && ch < pageData.getCropBoxHeight(leftPage+1)) {
                                ch = pageData.getCropBoxHeight(leftPage + 1);
                            }
                        }
                        break;
                    default:
                        if (isRotated) {
                            cw = pageData.getCropBoxHeight(page);
                            ch = pageData.getCropBoxWidth(page);
                        }else{
                            cw = pageData.getCropBoxWidth(page);
                            ch = pageData.getCropBoxHeight(page);
                        }
                }

                if(isSingle && displayPane!=null){
                        width -= displayPane.getDividerSize();
                    }

                float x_factor,y_factor,window_factor = 1.0f;

                // [AWI]: Iterate over the scaling algorithm to try and take the scrollbar size into account.
                // This logic was added to address an issue with 'Fit Width' and 'Fit Height' calculations where the 
                // scrollbar appeared as a result of the scaling, but the scrollbar was not taken into account in the 
                // scaling algorithm. When this occurred, both scrollbars could appear, even though only one should.
                //
                // The loopCount is configured such that the scaling routine should be run at least once, but not more 
                // than enough to make room for the scrollbar, to prevent an infinite loop.
                float scaledWidth, scaledHeight;
                int loopCount = 0;
                while ( loopCount++ <= (scrollbarWidth) ) {
                	x_factor = width / cw;
                	y_factor = height / ch;

                	if(x_factor<y_factor) {
                        window_factor = x_factor;
                    } else {
                        window_factor = y_factor;
                    }

                	if(index!=-1){
                		if(index<3){ //handle scroll to width/height/window
                			if(index==0){//window
                				scaling = window_factor;
                				break;
                			}else if(index==1){//height
                				scaling = y_factor;
                			}else if(index==2){//width
                				scaling = x_factor;
                			}

                			// [AWI]: Determine if the horizontal scrollbar will appear when set to 'Fit Height' 
                			// or if the vertical scrollbar will appear when set to 'Fit Width' and if so, add the 
                			// scrollbar size into the scaling algorithm and recalculate once.
                			scaledWidth = scaling * cw;
                			scaledHeight = scaling * ch;

                			if(index==1 && scaledWidth > width) {//height
                				height--;
                			}else if(index==2 && scaledHeight > height){//width
                				width--;
                			}else{
                				// Break out to prevent multiple passes since scrollbars should not be displayed
                				break;
                			}
                		}else{
                			scaling=decode_pdf.getDPIFactory().adjustScaling(scalingFloatValues[index]);
                			// Break out to prevent multiple passes since scrollbars should not be displayed
                			break;
                		}
                	}
                }
                if (decode_pdf.getDisplayView() == Display.FACING) { //Enable turnover if both pages properly displayed
                    pageTurnScalingAppropriate = scaling <= window_factor;
                }

                if(thumbscroll!=null){
                    if(decode_pdf.getDisplayView() == Display.SINGLE_PAGE && scaling<=window_factor){

                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                        thumbscroll.setVisible(true);
                    }else{

                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                        thumbscroll.setVisible(false);
                    }
                }
            }

            //this check for 0 to avoid error  and replace with 1
            //PdfPageData pagedata = decode_pdf.getPdfPageData();
			//if((pagedata.getCropBoxHeight(commonValues.getCurrentPage())*scaling<100) &&//keep the page bigger than 100 pixels high
			//        (pagedata.getCropBoxWidth(commonValues.getCurrentPage())*scaling<100) && commonValues.isPDF()){//keep the page bigger than 100 pixels wide
			//    scaling=1;
			//    setSelectedComboItem(Commands.SCALING,"100");
			//}

			// THIS section commented out so altering scalingbox does NOT reset rotation
			//if(!scalingBox.getSelectedIndex()<3){
			/**update our components*/
			//resetRotationBox();
			//}
			
			//Ensure page rotation is taken into account
			//int pageRot = decode_pdf.getPdfPageData().getRotation(commonValues.getCurrentPage());
			//allow for clicking on it before page opened
			decode_pdf.setPageParameters(scaling, page,rotation);
			
			//Ensure the page is displayed in the correct rotation
			setRotation();

			//move to correct page
			//setPageNumber();
			//decode_pdf.setDisplayView(decode_pdf.getDisplayView(),Display.DISPLAY_CENTERED);

			//open new page
			//if((!commonValues.isProcessing())&&(commonValues.getCurrentPage()!=newPage)){

			//commonValues.setCurrentPage(newPage);
			//decodePage(false);
			//currentGUI.zoom();
			//}

			//ensure at same page location

			final Runnable updateAComponent = new Runnable() {
				@Override
                public void run() {
					//
					((PdfDecoder)decode_pdf).invalidate();
					((PdfDecoder)decode_pdf).updateUI();
					((PdfDecoder)decode_pdf).validate();

					scrollPane.invalidate();
					scrollPane.updateUI();
					scrollPane.validate();

					//move to correct page
					//scrollToPage is handled via the page change code so no need to do it here
//					if(commonValues.isPDF())
//					scrollToPage(commonValues.getCurrentPage());
					//scrollPane.getViewport().scrollRectToVisible(new Rectangle((int)(x*scaling)-1,(int)(y*scaling),1,1));
					//System.out.println("Scroll to page="+y+" "+(y*scaling)+" "+scaling);

				}
			};
			//boolean callAsThread=SwingUtilities.isEventDispatchThread();
			//if (callAsThread)
			//	scroll
			SwingUtilities.invokeLater(updateAComponent);
//			else{

//			//move to correct page
//			if(commonValues.isPDF())
//			scrollToPage(commonValues.getCurrentPage());

//			scrollPane.updateUI();

//			}
			//decode_pdf.invalidate();
			//scrollPane.updateUI();
			//decode_pdf.repaint();
			//scrollPane.repaint();
			//frame.validate();


		}


	}

    @Override
    public void snapScalingToDefaults(float newScaling) {
        newScaling = decode_pdf.getDPIFactory().adjustScaling(newScaling /100);

        float width;
        final float height;

        if(isSingle){
            width = scrollPane.getViewport().getWidth()-inset-inset;
            height = scrollPane.getViewport().getHeight()-inset-inset;
        }else{
            width=desktopPane.getWidth();
            height=desktopPane.getHeight();
        }

        final PdfPageData pageData = decode_pdf.getPdfPageData();
        int cw,ch,raw_rotation=0;

        if (decode_pdf.getDisplayView()==Display.FACING) {
            raw_rotation = pageData.getRotation(commonValues.getCurrentPage());
        }

        final boolean isRotated = (rotation+raw_rotation)%180==90;

        final PageOffsets offsets = (PageOffsets)decode_pdf.getExternalHandler(Options.CurrentOffset);
        switch(decode_pdf.getDisplayView()) {
            case Display.CONTINUOUS_FACING:
                if (isRotated) {
                    cw = offsets.getMaxH()*2;
                    ch = offsets.getMaxW();
                }else{
                    cw = offsets.getMaxW()*2;
                    ch = offsets.getMaxH();
                }
                break;
            case Display.CONTINUOUS:
                if (isRotated) {
                    cw = offsets.getMaxH();
                    ch = offsets.getMaxW();
                }else{
                    cw = offsets.getMaxW();
                    ch = offsets.getMaxH();
                }
                break;
            case Display.FACING:
                int leftPage;
                if (decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) {
                    leftPage = (commonValues.getCurrentPage()/2)*2;
                    if (commonValues.getPageCount() == 2) {
                        leftPage = 1;
                    }
                } else {
                    leftPage = commonValues.getCurrentPage();
                    if ((leftPage & 1)==0) {
                        leftPage--;
                    }
                }

                if (isRotated) {
                    cw = pageData.getCropBoxHeight(leftPage);

                    //if first or last page double the width, otherwise add other page width
                    if (leftPage+1 > commonValues.getPageCount() || leftPage == 1) {
                        cw *= 2;
                    } else {
                        cw += pageData.getCropBoxHeight(leftPage + 1);
                    }

                    ch = pageData.getCropBoxWidth(leftPage);
                    if (leftPage+1 <= commonValues.getPageCount() && ch < pageData.getCropBoxWidth(leftPage+1)) {
                        ch = pageData.getCropBoxWidth(leftPage + 1);
                    }
                }else{
                    cw = pageData.getCropBoxWidth(leftPage);

                    //if first or last page double the width, otherwise add other page width
                    if (leftPage+1 > commonValues.getPageCount()) {
                        cw *= 2;
                    } else {
                        cw += pageData.getCropBoxWidth(leftPage + 1);
                    }

                    ch = pageData.getCropBoxHeight(leftPage);
                    if (leftPage+1 <= commonValues.getPageCount() && ch < pageData.getCropBoxHeight(leftPage+1)) {
                        ch = pageData.getCropBoxHeight(leftPage + 1);
                    }
                }
                break;
            default:
                if (isRotated) {
                    cw = pageData.getCropBoxHeight(commonValues.getCurrentPage());
                    ch = pageData.getCropBoxWidth(commonValues.getCurrentPage());
                }else{
                    cw = pageData.getCropBoxWidth(commonValues.getCurrentPage());
                    ch = pageData.getCropBoxHeight(commonValues.getCurrentPage());
                }
        }

        if(isSingle && displayPane!=null) {
                width -= displayPane.getDividerSize();
            }
        
        float x_factor;
        float y_factor;
        final float window_factor;
        x_factor = width / cw;
        y_factor = height / ch;

        if(x_factor<y_factor) {
            window_factor = x_factor;
            x_factor = -1;
        } else {
            window_factor = y_factor;
            y_factor = -1;
        }

        if (getSelectedComboIndex(Commands.SCALING)!=0 &&
                ((newScaling < window_factor * 1.1 && newScaling > window_factor *0.91) ||
                ((window_factor > scaling && window_factor < newScaling) || (window_factor < scaling && window_factor > newScaling)))) {
            setSelectedComboIndex(Commands.SCALING, 0);
            scaling = window_factor;
        }

        else if (y_factor!=-1 &&
                getSelectedComboIndex(Commands.SCALING)!=1 &&
                ((newScaling < y_factor * 1.1 && newScaling > y_factor * 0.91) ||
                ((y_factor > scaling && y_factor < newScaling) || (y_factor < scaling && y_factor > newScaling)))) {
            setSelectedComboIndex(Commands.SCALING, 1);
            scaling = y_factor;
        }

        else if (x_factor!=-1 &&
                getSelectedComboIndex(Commands.SCALING)!=2 &&
                ((newScaling < x_factor * 1.1 && newScaling > x_factor * 0.91) ||
                ((x_factor > scaling && x_factor < newScaling) || (x_factor < scaling && x_factor > newScaling)))) {
            setSelectedComboIndex(Commands.SCALING, 2);
            scaling = x_factor;
        }

        else {
            setSelectedComboItem(Commands.SCALING, String.valueOf((int)decode_pdf.getDPIFactory().removeScaling(newScaling *100)));
            scaling = newScaling;
        }
    }

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#rotate()
	 */
	@Override
    public void rotate() {
		rotation=Integer.parseInt((String) getSelectedComboItem(Commands.ROTATION));
		scaleAndRotate();
		((PdfDecoder)decode_pdf).updateUI();

	}

    
    @Override
	public void scrollToPage(final int page){

		commonValues.setCurrentPage(page);

		if(commonValues.getCurrentPage()>0){

			int yCord =0;
			int xCord =0;

			if(decode_pdf.getDisplayView()!=Display.SINGLE_PAGE){
    			yCord = decode_pdf.getPages().getYCordForPage(commonValues.getCurrentPage(),scaling);
				xCord = 0;
			}
			//System.out.println("Before="+decode_pdf.getVisibleRect()+" "+decode_pdf.getPreferredSize());

			final PdfPageData pageData = decode_pdf.getPdfPageData();

			final int ch = (int)(pageData.getCropBoxHeight(commonValues.getCurrentPage())*scaling);
			final int cw = (int)(pageData.getCropBoxWidth(commonValues.getCurrentPage())*scaling);

			final int centerH = xCord + ((cw-scrollPane.getHorizontalScrollBar().getVisibleAmount())/2);
			final int centerV = yCord + (ch-scrollPane.getVerticalScrollBar().getVisibleAmount())/2;

			scrollPane.getHorizontalScrollBar().setValue(centerH);
			scrollPane.getVerticalScrollBar().setValue(centerV);



//			decode_pdf.scrollRectToVisible(new Rectangle(0,(int) (yCord),(int)r.width-1,(int)r.height-1));
//			decode_pdf.scrollRectToVisible(new Rectangle(0,(int) (yCord),(int)r.width-1,(int)r.height-1));

			//System.out.println("After="+decode_pdf.getVisibleRect()+" "+decode_pdf.getPreferredSize());

			//System.out.println("Scroll to page="+commonValues.getCurrentPage()+" "+yCord+" "+(yCord*scaling)+" "+scaling);
		}

		if(decode_pdf.getPageCount()>1) {
            swButtons.setPageLayoutButtonsEnabled(true);
        }
		
	}

	

//	<link><a name="listen" />

	/**
	 *  put the outline data into a display panel which we can pop up
	 * for the user - outlines, thumbnails
	 *
	private void createOutlinePanels() {

		//boolean hasNavBars=false;

		// set up first 10 thumbnails by default. Rest created as needed.
		
		//add if statement or comment out this section to remove thumbnails
		setupThumbnailPanel();

		// add any outline
		
		setBookmarks(false);

		/**
		 * resize to show if there are nav bars
		 *
        if(hasNavBars){
            if(!thumbnails.isShownOnscreen()){
                if( !commonValues.isContentExtractor())
                navOptionsPanel.setVisible(true);
                displayPane.setDividerLocation(divLocation);
                //displayPane.invalidate();
                //displayPane.repaint();

            }
        }
	}/**/

	//	<start-thin>
    @Override
	public void setupThumbnailPanel() {

		decode_pdf.addExternalHandler(thumbnails, Options.ThumbnailHandler);

		if(isSetup) {
            return;
        }

		isSetup=true;

		if(thumbnails.isShownOnscreen()){

			final int pages=decode_pdf.getPageCount();

			//setup and add to display

			thumbnails.setupThumbnails(pages,textFont, Messages.getMessage("PdfViewerPageLabel.text"),decode_pdf.getPdfPageData());

			//add listener so clicking on button changes to page - has to be in Viewer so it can update it
			final Object[] buttons=thumbnails.getButtons();
			for(int i=0;i<pages;i++) {
                ((JButton) buttons[i]).addActionListener(new SwingPageChanger(this, commonValues, i));
            }

			//add global listener
			thumbnails.addComponentListener();

		}
	}
	//	<end-thin>

    @Override
	public void setBookmarks(final boolean alwaysGenerate) {

		//ignore if not opened
		final int currentSize=displayPane.getDividerLocation();

		if((currentSize==collapsedSize)&& !alwaysGenerate) {
            return;
        }


        //ignore if already done and flag
        if(bookmarksGenerated){
            return;
        }
        bookmarksGenerated=true;
        
		final org.w3c.dom.Document doc=decode_pdf.getOutlineAsXML();

		Node rootNode= null;
		if(doc!=null) {
            rootNode = doc.getFirstChild();
        }

		if(rootNode!=null){

			tree.reset(rootNode);

			/*
			 * Use a mouse listener instead of a TreeSelectionListener for 2 reasons
			 * 1. Tree selection only works on new selections, Mouse clicks work on
			 *    every click of the mouse.
			 * 2. Tree nodes are highlighted before the mouse event, the mouse event
			 *    can check the node that was highlighted by the tree and trigger it
			 *    allowing highlighted bookmarks to be triggered again.
			 *    We can also check if the mouse is within the node so it doesn't trigger
			 *    if clicked in an empty area.
			 */
			((JTree) tree.getTree()).addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(final MouseEvent e) {}
				@Override
				public void mousePressed(final MouseEvent e) {
					final DefaultMutableTreeNode node = tree.getLastSelectedPathComponent();
					
					if(((JTree) tree.getTree()).getSelectionRows().length!=0){
						final Rectangle r = ((JTree) tree.getTree()).getRowBounds(((JTree) tree.getTree()).getSelectionRows()[0]);

						if (node == null || !r.contains(e.getX(), e.getY())) {
                            return;
                        }
					}

					triggerBookmark(node);
				}
				@Override
				public void mouseExited(final MouseEvent e) {}
				@Override
				public void mouseEntered(final MouseEvent arg0) {}
				@Override
				public void mouseClicked(final MouseEvent arg0) {
					
					
				}
			});
			
			/*
			 * Navigate bookmarks using arrow keys,
			 * left and right go to parent / child nodes,
			 * enter key will trigger the currently selected node
			 */
			((JTree)tree.getTree()).addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(final KeyEvent e) {}
				
				@Override
				public void keyReleased(final KeyEvent e) {
					if(e.getKeyCode()=='\n'){

						
						final DefaultMutableTreeNode node = tree.getLastSelectedPathComponent();
						
						if (node == null) {
                            return;
                        }

						triggerBookmark(node);
					
					}
				}
				
				@Override
				public void keyPressed(final KeyEvent e) {}
			});
			
		}else{
			tree.reset(null);
		}
	}

    private void triggerBookmark(final DefaultMutableTreeNode node){
    	
    	/**get title and open page if valid*/
		//String title=(String)node.getUserObject();

		final JTree jtree = ((JTree) tree.getTree());

		final DefaultTreeModel treeModel = (DefaultTreeModel) jtree.getModel();

        final java.util.List flattenedTree = new ArrayList();

		/** flatten out the tree so we can find the index of the selected node */
		getFlattenedTreeNodes((TreeNode) treeModel.getRoot(), flattenedTree);
		flattenedTree.remove(0); // remove the root node as we don't account for this

		final int index = flattenedTree.indexOf(node);
        
		//String page = tree.getPageViaNodeNumber(index);
		final String ref = tree.convertNodeIDToRef(index);

        final PdfObject Aobj=decode_pdf.getOutlineData().getAobj(ref);

        //handle in our handler code
        if(Aobj!=null){
        	if(SwingUtilities.isEventDispatchThread())
            /*int pageToDisplay=*/ {
                decode_pdf.getFormRenderer().getActionHandler().gotoDest(Aobj, ActionHandler.MOUSECLICKED, PdfDictionary.Dest);
            } else{
        		SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						decode_pdf.getFormRenderer().getActionHandler().gotoDest(Aobj, ActionHandler.MOUSECLICKED, PdfDictionary.Dest );
					}
				});
        	}
            //align to viewer knows if page changed and set rotation to default for page
//            if(pageToDisplay!=-1){
//            	currentCommands.gotoPage(Integer.toString(pageToDisplay));
//                commonValues.setCurrentPage(pageToDisplay);
//                setSelectedComboIndex(Commands.ROTATION, decode_pdf.getPdfPageData().getRotation(pageToDisplay)/90); 
//            }
        }
        
//        if((page==null)||(page.length()==0))
//        page=tree.getPage(title);
//
//        if(page!=null && page.length()>0){
//			int pageToDisplay=Integer.parseInt(page);
//
//			if((!commonValues.isProcessing())&&(commonValues.getCurrentPage()!=pageToDisplay)){
//				commonValues.setCurrentPage(pageToDisplay);
//				/**reset as rotation may change!*/
//
//				decode_pdf.setPageParameters(getScaling(), commonValues.getCurrentPage());
//				decodePage(false);
//			}
//
//			//Point p= tree.getPoint(title);
//			//if(p!=null)
//			//	decode_pdf.ensurePointIsVisible(p);
//
//		}else{
//			showMessageDialog(Messages.getMessage("PdfViewerError.NoBookmarkLink")+title);
//			System.out.println("No dest page set for "+title);
//		}
        
    }
    
    @Override
	public void selectBookmark() {
		if(decode_pdf.hasOutline()&&(tree!=null)) {
            tree.selectBookmark();
        }

	}

    private void initStatus() {
		decode_pdf.setStatusBarObject(statusBar);

        //and initialise the display

        setMultibox(new int[]{});
        
	}

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#setCoordText(java.lang.String)
	 */
	@Override
    public void setCoordText(final String string) {
		coords.setText(string);
	}

	private void initCoordBox() {

		coords.setBackground(Color.white);
		coords.setOpaque(true);

        coords.setBorder(BorderFactory.createEtchedBorder());
        coords.setPreferredSize(multibox.getPreferredSize());
        //Needed to ensure the coords appear correctly in facing mode
        //If coords have not been displayed before entering facing mode
        coords.setSize(multibox.getPreferredSize());
       

		coords.setText("  X: " + " Y: " + ' ' + ' ');

	}



	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#setPageNumber()
	 */
	@Override
    public void setPageNumber() {

        if (SwingUtilities.isEventDispatchThread()) {
            setPageNumberWorker();
            if(isSetup){
                //Page changed so save this page as last viewed
                setThumbnails();
            }
        } else {
            final Runnable r = new Runnable(){
                @Override
                public void run() {
                    setPageNumberWorker();
                    if(isSetup){
                        //Page changed so save this page as last viewed
                        setThumbnails();
                    }
                }
            };
            SwingUtilities.invokeLater(r);
        }
    }
	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#setPageNumber()
	 */
	private void setPageNumberWorker() {

        if(pageCounter2==null){
            return;
        }

		if (!decode_pdf.isOpen() && !commonValues.isMultiTiff()) {
            pageCounter2.setText("0");
            pageCounter3.setText(Messages.getMessage("PdfViewerOfLabel.text") + " 0");
        } else {

            if(previewOnSingleScroll && thumbscroll!=null){
            	
                thumbscroll.setMaximum(decode_pdf.getPageCount());
                thumbscroll.setValue(commonValues.getCurrentPage()-1);
                
                if(debugThumbnail) {
                    System.out.println("setpage=" + commonValues.getCurrentPage());
                }
            }

            final int currentPage = commonValues.getCurrentPage();
            if (decode_pdf.getDisplayView() == Display.FACING || decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING) {
                if (decode_pdf.getPageCount() == 2) {
                    pageCounter2.setText("1/2");
                } else if (decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER) || decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING) {
                    final int base = currentPage & -2;
                    if (base != decode_pdf.getPageCount() && base != 0) {
                        pageCounter2.setText(base + "/" + (base + 1));
                    } else {
                        pageCounter2.setText(String.valueOf(currentPage));
                    }
                } else {
                    final int base = currentPage - (1 - (currentPage & 1));
                    if (base != decode_pdf.getPageCount()) {
                        pageCounter2.setText(base + "/" + (base + 1));
                    } else {
                        pageCounter2.setText(String.valueOf(currentPage));
                    }
                }

            } else {
                pageCounter2.setText(String.valueOf(currentPage));
            }
            pageCounter3.setText(Messages.getMessage("PdfViewerOfLabel.text") + ' ' + commonValues.getPageCount()); //$NON-NLS-1$
            swButtons.hideRedundentNavButtons(this);
        }
	}
    
    /**
     * note - to plugin put all on single line so addButton values over-riddern
     */
	private void createNavbar() {

        final java.util.List v = new ArrayList();

        if(memoryMonitor==null){ //ensure only 1 instance
            memoryMonitor = new Timer(500, new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent event) {
                    final int free = (int) (Runtime.getRuntime().freeMemory() / (1024 * 1024));
                    final int total = (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024));

                    //this broke the image saving when it was run every time
                    if(finishedDecoding){
                        finishedDecoding=false;
                    }

                    //System.out.println((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1000);
                    memoryBar.setMaximum(total);
                    memoryBar.setValue(total-free);
                    memoryBar.setStringPainted(true);
                    memoryBar.setString((total-free)+"M of "+total+ 'M');
                }
            });
            memoryMonitor.start();
        }

        multibox.setLayout(new BorderLayout());

        if(commonValues.getModeOfOperation()!=Values.RUNNING_PLUGIN) {
            navButtons.add(multibox, BorderLayout.WEST);
        }

		navButtons.add(Box.createHorizontalGlue());

		/**
		 * navigation toolbar for moving between pages
		 */
        navToolBar.add(Box.createHorizontalGlue());

		swButtons.addButton(NAVBAR,Messages.getMessage("PdfViewerNavBar.RewindToStart"),"start.gif",Commands.FIRSTPAGE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);


		swButtons.addButton(NAVBAR,Messages.getMessage("PdfViewerNavBar.Rewind10"),"fback.gif",Commands.FBACKPAGE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);


		swButtons.addButton(NAVBAR,Messages.getMessage("PdfViewerNavBar.Rewind1"),"back.gif",Commands.BACKPAGE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

		/**put page count in middle of forward and back*/
        pageCounter1 = new JLabel(Messages.getMessage("PdfViewerPageLabel.text"));
        pageCounter1.setOpaque(false);
		navToolBar.add(pageCounter1);
//		pageCounter2.setMaximumSize(new Dimension(5,50));
		navToolBar.add(pageCounter2);
		navToolBar.add(pageCounter3);

		swButtons.addButton(NAVBAR,Messages.getMessage("PdfViewerNavBar.Forward1"),"forward.gif",Commands.FORWARDPAGE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

		swButtons.addButton(NAVBAR,Messages.getMessage("PdfViewerNavBar.Forward10"),"fforward.gif",Commands.FFORWARDPAGE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

		swButtons.addButton(NAVBAR,Messages.getMessage("PdfViewerNavBar.ForwardLast"),"end.gif",Commands.LASTPAGE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);


		navToolBar.add(Box.createHorizontalGlue());

		//add buttons but not in Content Extractor
		if(isSingle){
			swButtons.addButton(PAGES,Messages.getMessage("PageLayoutButton.SinglePage"),"single.gif",Commands.SINGLE, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

			swButtons.addButton(PAGES,Messages.getMessage("PageLayoutButton.Continuous"),"continuous.gif",Commands.CONTINUOUS, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

			swButtons.addButton(PAGES,Messages.getMessage("PageLayoutButton.ContinousFacing"),"continuous_facing.gif",Commands.CONTINUOUS_FACING, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

			swButtons.addButton(PAGES,Messages.getMessage("PageLayoutButton.Facing"),"facing.gif",Commands.FACING, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);

                        swButtons.addButton(PAGES,Messages.getMessage("PageLayoutButton.PageFlow"),"pageflow.gif",Commands.PAGEFLOW, menuItems, this, currentCommandListener, pagesToolBar, navToolBar);
		}
		
        final Dimension size;
        
        //on top in plugin
        if(commonValues.getModeOfOperation()==Values.RUNNING_PLUGIN) {
            swButtons.getTopButtons().add(pagesToolBar, BorderLayout.EAST);
        } else {
            navButtons.add(pagesToolBar, BorderLayout.EAST);
        }

        size = pagesToolBar.getPreferredSize();
 
        multibox.setPreferredSize(size);

		final boolean[] defaultValues = new boolean[v.size()];
		for(int i=0; i!=v.size(); i++){
            defaultValues[i] = v.get(i).equals(Boolean.TRUE);
		}

//		p.setNavDefaults(defaultValues);



        //on top in plugin
        if(commonValues.getModeOfOperation()==Values.RUNNING_PLUGIN) {
            swButtons.getTopButtons().add(navToolBar, BorderLayout.CENTER);
        } else {
            navButtons.add(navToolBar, BorderLayout.CENTER);
        }

	}

	@Override
    public void setPage(int page){

        if (((decode_pdf.getDisplayView() == Display.FACING && decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) ||
                decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING) &&
                (page & 1) == 1 && page != 1) {
            page--;
        } else if (decode_pdf.getDisplayView() == Display.FACING  && !decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER) &&
                (page & 1) == 0) {
            page--;
        }

		commonValues.setCurrentPage(page);
        setPageNumber();
		
	}

    @Override
    public Enum getType() {
        return GUIModes.SWING;
    }

    @Override
    public void resetPageNav() {
		pageCounter2.setText("");
		pageCounter3.setText("");
	}



    @Override
	public void setRotationFromExternal(final int rot){
		rotation = rot;
		rotationBox.setSelectedIndex(rotation / 90);
		if(!Values.isProcessing()){
            //<start-fx>
            ((PdfDecoder)decode_pdf).repaint();
            //<end-fx>
		}
	}

    @Override
	public void setScalingFromExternal(final String scale){

        if(scale.startsWith("Fit ")){ //allow for Fit Page, Fit Width, Fit Height
            scalingBox.setSelectedItem(scale);
        }else{
            scalingBox.setSelectedItem(scale + '%');
        }
	}
    
    


	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#getFrame()
	 */
	@Override
    public Container getFrame() {
		return frame;
	}
    
    /* (non-Javadoc)
     * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showMessageDialog(java.lang.Object)
     */
    @Override
    public void showMessageDialog(final String message1){

        /**
         * allow user to replace messages with our action
         */
        boolean showMessage=true;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            showMessage = customMessageHandler.showMessage(message1);
        }

        if(showMessage) {
            JOptionPane.showMessageDialog(frame, message1);
        }

	}

    @Override
    /* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showMessageDialog(java.lang.Object)
	 */
    public int showMessageDialog(final Object message1, final Object[] options, final int selectedChoice){

        int n=0;
        /**
         * allow user to replace messages with our action
         */
        boolean showMessage=true;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            showMessage = customMessageHandler.showMessage(message1);
        }

        if(showMessage){
            n=JOptionPane.showOptionDialog(frame, message1, "Message",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[selectedChoice]);
        }

        return n;

    }

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showMessageDialog(java.lang.Object, java.lang.String, int)
	 */
	@Override
    public void showMessageDialog(final Object message, final String title, final int type){

        /**
         * allow user to replace messages with our action
         */
        boolean showMessage=true;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            showMessage = customMessageHandler.showMessage(message);
        }

        if(showMessage) {
            JOptionPane.showMessageDialog(frame, message, title, type);
        }
	}


	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showInputDialog(java.lang.Object, java.lang.String, int)
	 */
	@Override
    public String showInputDialog(final Object message, final String title, final int type) {

        /**
         * allow user to replace messages with our action
         */
        String returnMessage=null;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            returnMessage = customMessageHandler.requestInput(new Object[]{message, title, title});
        }

        if(returnMessage==null) {
            return JOptionPane.showInputDialog(frame, message, title, type);
        } else {
            return returnMessage;
        }
	}

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showInputDialog(java.lang.String)
	 */
	@Override
    public String showInputDialog(final String message) {

        /**
         * allow user to replace messages with our action
         */
        String returnMessage=null;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            returnMessage = customMessageHandler.requestInput(new String[]{message});
        }

        if(returnMessage==null) {
            return JOptionPane.showInputDialog(frame, message);
        } else {
            return returnMessage;
        }
	}
	
	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showConfirmDialog(java.lang.String, java.lang.String, int)
	 */
	@Override
    public int showConfirmDialog(final String message, final String message2, final int option) {

        /**
         * allow user to replace messages with our action
         */
        int returnMessage=-1;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            returnMessage = customMessageHandler.requestConfirm(new Object[]{message, message2, String.valueOf(option)});
        }

        if(returnMessage==-1) {
            return JOptionPane.showConfirmDialog(frame, message, message2, option);
        } else {
            return returnMessage;
        }
	}

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showOverwriteDialog(String file,boolean yesToAllPresent)
	 */
	@Override
    public int showOverwriteDialog(final String file, final boolean yesToAllPresent) {

		final int n;

        /**
         * allow user to replace messages with our action and remove popup
         */
        int returnMessage=-1;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            returnMessage = customMessageHandler.requestConfirm(new Object[]{file, String.valueOf(yesToAllPresent)});
        }

        if(returnMessage!=-1) {
            return returnMessage;
        }

		if(yesToAllPresent){

			final Object[] buttonRowObjects = {
					Messages.getMessage("PdfViewerConfirmButton.Yes"),
					Messages.getMessage("PdfViewerConfirmButton.YesToAll"),
					Messages.getMessage("PdfViewerConfirmButton.No"),
					Messages.getMessage("PdfViewerConfirmButton.Cancel")
			};

			n = JOptionPane.showOptionDialog(frame,
					file+ '\n' +Messages.getMessage("PdfViewerMessage.FileAlreadyExists")
					+ '\n' +Messages.getMessage("PdfViewerMessage.ConfirmResave"),
					Messages.getMessage("PdfViewerMessage.Overwrite"),
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					buttonRowObjects,
					buttonRowObjects[0]);

		}else{
			n = JOptionPane.showOptionDialog(frame,
					file+ '\n' +Messages.getMessage("PdfViewerMessage.FileAlreadyExists")
					+ '\n' +Messages.getMessage("PdfViewerMessage.ConfirmResave"),
					Messages.getMessage("PdfViewerMessage.Overwrite"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,null,null);
		}

		return n;
	}

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showMessageDialog(javax.swing.JTextArea)
	 */
	@Override
    public void showMessageDialog(final Object info) {

        /**
         * allow user to replace messages with our action
         */
        boolean showMessage=true;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            showMessage = customMessageHandler.showMessage(info);
        }

        if(showMessage) {
            JOptionPane.showMessageDialog(frame, info);
        }

	}

	@Override
    public void showFirstTimePopup(){

        //allow user to disable
        final boolean showMessage=(customMessageHandler!=null && customMessageHandler.showMessage("first time popup")) ||
                customMessageHandler==null;

        if(!showMessage || commonValues.getModeOfOperation()==Values.RUNNING_APPLET) {
            return;
        }

		try{
			final JPanel a = new JPanel();
			a.setLayout(new BoxLayout(a, BoxLayout.Y_AXIS));

            final MouseAdapter supportListener = new MouseAdapter() {
                @Override
                public void mouseEntered(final MouseEvent e) {
                	if(GUIDisplay.allowChangeCursor) {
                        a.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                	if(GUIDisplay.allowChangeCursor) {
                        a.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }

                @Override
                public void mouseClicked(final MouseEvent e) {
                    try {
                        BrowserLauncher.openURL(Messages.getMessage("PdfViewer.SupportLink.Link"));
                    } catch (final Exception e1) {
                        showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
                    }
                }
            };

			final JLabel img=new JLabel(new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/supportScreenshot.png")));
            img.setBorder(BorderFactory.createRaisedBevelBorder());
            img.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            img.addMouseListener(supportListener);
            a.add(img);

            final JLabel supportLink=new JLabel("<html><center><u>"+Messages.getMessage("PdfViewer.SupportLink.Text1")+ ' ' +Messages.getMessage("PdfViewer.SupportLink.Text2")+"</u></html>");
            supportLink.setMaximumSize(new Dimension(245,60));
            supportLink.setForeground(Color.BLUE);
            supportLink.addMouseListener(supportListener);
            supportLink.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            a.add(supportLink);
            a.add(Box.createRigidArea(new Dimension(10,10)));

			JOptionPane.showMessageDialog(
						frame,
						a,
						Messages.getMessage("PdfViewerTitle.RunningFirstTime"),
						JOptionPane.PLAIN_MESSAGE);
		}catch(final Exception e){
			//JOptionPane.showMessageDialog(null, "caught an exception "+e);
			System.err.println(Messages.getMessage("PdfViewerFirstRunDialog.Error")+' '+e);
		}catch(final Error e){
			//JOptionPane.showMessageDialog(null, "caught an error "+e);
			System.err.println(Messages.getMessage("PdfViewerFirstRunDialog.Error")+' '+e);
		}
	}

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#showConfirmDialog(java.lang.Object, java.lang.String, int, int)
	 */
	@Override
    public int showConfirmDialog(final Object message, final String title, final int optionType, final int messageType) {

        /**
         * allow user to replace messages with our action
         */
        int returnMessage=-1;

        //check user has not setup message and if we still show message
        if(customMessageHandler !=null) {
            returnMessage = customMessageHandler.requestConfirm(new Object[]{message, title, String.valueOf(optionType), String.valueOf(messageType)});
        }

        if(returnMessage==-1) {
            return JOptionPane.showConfirmDialog(frame, message, title, optionType, messageType);
        } else {
            return returnMessage;
        }
	}

    /* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#updateStatusMessage(java.lang.String)
	 */
    @Override
	public void setDownloadProgress(final String message, final int percentage) {
        downloadBar.setProgress(message, percentage);

        setMultibox(new int[]{});
	}

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#updateStatusMessage(java.lang.String)
	 */
	@Override
    public void updateStatusMessage(final String message) {
		statusBar.updateStatus(message,0);
	}

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#resetStatusMessage(java.lang.String)
	 */
	@Override
    public void resetStatusMessage(final String message) {
		statusBar.resetStatus(message);

	}

	/* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#setStatusProgress(int)
	 */
	@Override
    public void setStatusProgress(final int size) {
		statusBar.setProgress(size);

        setMultibox(new int[]{});
	}
	
    @Override
	public int getSplitDividerLocation(){
		return displayPane.getDividerLocation();
	}

	private PrintPanel printPanel;

	@Override
    public Object printDialog(final String[] printersList, final String defaultPrinter) {

		final JDialog printDialog = new JDialog((JFrame)null, Messages.getMessage("PdfViewerLabel.Printer"), true);
		printDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //get default resolution
        String propValue = properties.getValue("defaultDPI");
        int defaultDPI = -1;
        if (propValue != null && !propValue.isEmpty()) {
            try {
                propValue = propValue.replaceAll("[^0-9]", "");
                defaultDPI = Integer.parseInt(propValue);
            } catch (final Exception e) {
                if (LogWriter.isOutput()) {
                    LogWriter.writeLog("Caught an Exception " + e);
                }
            }
        }

		if(printPanel==null){
			printPanel = new PrintPanel(printersList,defaultPrinter, getPaperSizes(), defaultDPI, commonValues.getCurrentPage(), decode_pdf);
			//System.out.println("New print panel!!!!");
		}else {
            printPanel.resetDefaults(printersList, defaultPrinter, commonValues.getPageCount(), commonValues.getCurrentPage());
        }

		printDialog.getContentPane().add(printPanel);

		printDialog.setSize(670, 415);
        printDialog.setResizable(false);
        //printDialog.setIconImage(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
		printDialog.setLocationRelativeTo(frame);
		printDialog.setName("printDialog");
		printDialog.setVisible(true);

		printDialog.remove(printPanel);

		return printPanel;
	}

    @Override
    public PaperSizes getPaperSizes() {
        if (paperSizes==null) {
            paperSizes = new PaperSizes(properties.getValue("defaultPagesize"));
        }
        return paperSizes;
    }


	@Override
    public void setQualityBoxVisible(final boolean visible){
		
		if(properties.getValue("Imageopdisplay").toLowerCase().equals("true") &&
				qualityBox!=null && optimizationLabel!=null){
			qualityBox.setVisibility(visible);
			optimizationLabel.setVisible(visible);
		}
	}

	private void setThumbnails() {
        final org.jpedal.utils.SwingWorker worker = new org.jpedal.utils.SwingWorker() {
			@Override
            public Object construct() {

				if(thumbnails.isShownOnscreen()) {
					setupThumbnailPanel();
                    
                    thumbnails.generateOtherVisibleThumbnails(commonValues.getCurrentPage());
				}

				return null;
			}
		};
		worker.start();
	}

    @Override
	public void setSearchText(final Object searchText) {
		this.searchText = (JTextField)searchText;
	}

    @Override
	public void setResults(final GUISearchList results) {
		this.results = results;
		
		if(searchInMenu && this.results.getResultCount()==0) {
            showMessageDialog(Messages.getMessage("PdfViewerFileMenuFind.noResultText") + " \"" + results.getSearchTerm() + '"', Messages.getMessage("PdfViewerFileMenuFind.noResultTitle"), JOptionPane.INFORMATION_MESSAGE);
        }
	}
	
    @Override
	public Object getSideTabBar() {
		return navOptionsPanel;
	}

    /* (non-Javadoc)
	 * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#enableSearchItems(boolean)
	 */
	@Override
    public void enableSearchItems(final boolean enabled){
		if(searchInMenu){ //Menu Bar search
			searchText.setEnabled(enabled);
			options.setEnabled(enabled);
			swButtons.getButton(Commands.NEXTRESULT).setEnabled(false);
			swButtons.getButton(Commands.PREVIOUSRESULT).setEnabled(false);
		}else if(swButtons.getButton(Commands.FIND)!=null){ //External Window search
			swButtons.getButton(Commands.FIND).setEnabled(enabled);
		}
	}

//	<link><a name="exampledraw" />
	/**
	 * example of a custom draw object
	 *
	private static class ExampleCustomDrawObject implements JPedalCustomDrawObject {

		private boolean isVisible=true;

		private int page = 0;

		public int medX = 0;
		public int medY = 0;


		public ExampleCustomDrawObject(){

		}

		public ExampleCustomDrawObject(Integer option){

			if(option.equals(JPedalCustomDrawObject.ALLPAGES))
				page=-1;
			else throw new RuntimeException("Only valid setting is JPedalCustomDrawObject.ALLPAGES");
		}

		public int getPage(){
			return page;
		}


		public void print(Graphics2D g2, int x) {

			//custom code or just pass through
			if(page==x || page ==-1 || page==0)
				paint(g2);
		}

		public void paint(Graphics2D g2) {
			if(isVisible){

				//your code here

				//if you alter something, put it back
				Paint paint=g2.getPaint();

				//loud shape we can see
				g2.setPaint(Color.orange);
				g2.fillRect(100+medX,100+medY,100,100); // PDF co-ordinates due to transform

				g2.setPaint(Color.RED);
				g2.drawRect(100+medX,100+medY,100,100); // PDF co-ordinates due to transform

				//put back values
				g2.setPaint(paint);
			}
		}

        /**example onto rotated page
        public void paint(Graphics2D g2) {
                if(isVisible){

                    //your code here

                    AffineTransform aff=g2.getTransform();


                    //allow for 90 degrees - detect of G2
                    double[] matrix=new double[6];
                    aff.getMatrix(matrix);

                    //System.out.println("0="+matrix[0]+" 1="+matrix[1]+" 2="+matrix[2]+" 3="+matrix[3]+" 4="+matrix[4]+" 5="+matrix[5]);
                    if(matrix[1]>0 && matrix[2]>0){ //90

                        g2.transform(AffineTransform.getScaleInstance(-1, 1));
                        g2.transform(AffineTransform.getRotateInstance(90 *Math.PI/180));

                        //BOTH X and Y POSITIVE!!!!
                    g2.drawString("hello world", 60,60);
                    }else if(matrix[0]<0 && matrix[3]>0){ //180 degrees  (origin now top right)
                        g2.transform(AffineTransform.getScaleInstance(-1, 1));

                        g2.drawString("hello world", -560,60);//subtract cropW from first number to use standard values

                    }else if(matrix[1]<0 && matrix[2]<0){ //270

                        g2.transform(AffineTransform.getScaleInstance(-1, 1));
                        g2.transform(AffineTransform.getRotateInstance(-90 *Math.PI/180));

                        //BOTH X and Y NEGATIVE!!!!
                        g2.drawString("hello world", -560,-60); //subtract CropW and CropH if you want standard values
                    }else{ //0 degress
                        g2.transform(AffineTransform.getScaleInstance(1, -1));
                        // X ONLY POSITIVE!!!!
                        g2.drawString("hello world", 60,-60);
                    }

                    //restore!!!
                    g2.setTransform(aff);
                }
            }
        

		public void setVisible(boolean isVisible) {
			this.isVisible=isVisible;
		}

		public void setMedX(int medX) {
			this.medX = medX;
		}

		public void setMedY(int medY) {
			this.medY = medY;
		}
	}/**/
    
    
    @Override
	public void removeSearchWindow(final boolean justHide) {
		searchFrame.removeSearchWindow(justHide);
	}

    @Override
    public void alterProperty(final String value, final boolean set) {
        Properties.alterProperty(value, set, properties, this, isSingle, swButtons, menuItems);
    }
    
	@Override
    public void dispose(){

        swButtons.dispose();
		super.dispose();
        
        mouseHandler=null;

		pageTitle=null;
		bookmarksTitle=null;

		signaturesTitle=null;
		layersTitle=null;
        
		currentCommandListener=null;

		if(navButtons!=null) {
            navButtons.removeAll();
        }
		navButtons =null;

        menuItems.dispose();
        
		if(coords!=null) {
            coords.removeAll();
        }
		coords=null;

		if(frame!=null) {
            frame.removeAll();
        }
		frame=null;

		if(desktopPane!=null) {
            desktopPane.removeAll();
        }
		desktopPane=null;

		if(navOptionsPanel!=null) {
            navOptionsPanel.removeAll();
        }
		navOptionsPanel=null;

		if(scrollPane!=null) {
            scrollPane.removeAll();
        }
		scrollPane =null;

		headFont=null;

		textFont=null;

		statusBar=null;

        downloadBar=null;

		pageCounter2 =null;

		pageCounter3=null;

		optimizationLabel=null;

		if(signaturesTree!=null){
			signaturesTree.setCellRenderer(null);
			signaturesTree.removeAll();
		}
		signaturesTree=null;

		if(layersPanel!=null) {
            layersPanel.removeAll();
        }
		layersPanel=null;

        if(navToolBar!=null) {
            navToolBar.removeAll();
        }
		navToolBar =null;

		if(pagesToolBar!=null) {
            pagesToolBar.removeAll();
        }
		pagesToolBar =null;

		layersObject=null;

        //release memory at end
        if(memoryMonitor!=null){
            memoryMonitor.stop();
        }
	}

    @Override
    public boolean getPageTurnScalingAppropriate() {
        return pageTurnScalingAppropriate;
    }
    
    @Override
    public SwingCursor getGUICursor(){
        return guiCursor;
    }

    @Override
    public void rescanPdfLayers() {
        layersPanel.rescanPdfLayers();
	}
    
    @Override
    public String getTitles(final String title){
        if(title.equals(pageTitle)){
            return pageTitle;
        }else if(title.equals(bookmarksTitle)){
            return bookmarksTitle;
        }else if(title.equals(signaturesTitle)){
            return signaturesTitle;
        }else if(title.equals(layersTitle)){
            return layersTitle;
        }
        return null;
    }
    
    @Override
    public StatusBar getStatusBar(){
        return statusBar;
    }
    
    @Override
    public GUIButtons getButtons(){
        return swButtons;
    }

    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void setViewerIcon() {
        
        if (frame instanceof JFrame) {

            //Check if file location provided
            final URL path = guiCursor.getURLForImage("icon.png");
            if (path != null) {
                try {
                    final BufferedImage fontIcon = ImageIO.read(path);
                    ((JFrame) frame).setIconImage(fontIcon);
                } catch (final Exception e) {
                    if (LogWriter.isOutput()) {
                        LogWriter.writeLog("Exception attempting to set icon " + e);
                    }
                }
            }
        }
        
    }
    
    @Override
    public GUIMenuItems getMenuItems(){
        return menuItems;
    }
    
    @Override
    public void setTabsNotInitialised(final boolean b){
        tabsNotInitialised = b;
    }
    
    /**
     * Key method that calls decodePage from GUI.java
     */
    @Override
    public void decodePage(){
    	//Ensure thumbnail scroll bar is updated when page changed
        if (thumbscroll != null) {
            setThumbnailScrollBarValue(commonValues.getCurrentPage() - 1);
		}
        decodeGUIPage(this);
    }
        
    @Override
    public PdfDecoder getPdfDecoder(){
		return ((PdfDecoder)decode_pdf);
	}

    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void addComboListenerAndLabel(final GUICombo combo, final String title) {

        optimizationLabel = new JLabel(title);

        //add listener
        ((SwingCombo) combo).addActionListener(currentCommandListener.getSwingCommandListener());
    }

    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void addGUIComboBoxes(final GUICombo combo) {
        swButtons.getTopButtons().add((SwingCombo) combo);
    }
    
    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void setupCenterPanelBackground() {
        String propValue = properties.getValue("replacePdfDisplayBackground");
        if (!propValue.isEmpty()
                && propValue.toLowerCase().equals("true")) {
            //decode_pdf.useNewGraphicsMode = false;
            propValue = properties.getValue("pdfDisplayBackground");
            if (!propValue.isEmpty()) {
                currentCommands.executeCommand(Commands.SETDISPLAYBACKGROUND, new Object[]{Integer.parseInt(propValue)});
            }

            ((PdfDecoder) decode_pdf).setBackground(new Color(Integer.parseInt(propValue)));
            frame.setBackground(new Color(Integer.parseInt(propValue)));

        } else {
            if (decode_pdf.getDecoderOptions().getDisplayBackgroundColor() != null) {
                ((PdfDecoder) decode_pdf).setBackground(decode_pdf.getDecoderOptions().getDisplayBackgroundColor());
                frame.setBackground(decode_pdf.getDecoderOptions().getDisplayBackgroundColor());
            } else if (decode_pdf.useNewGraphicsMode()) {
                ((PdfDecoder) decode_pdf).setBackground(new Color(55, 55, 65));
                frame.setBackground(new Color(55, 55, 65));
            } else {
                ((PdfDecoder) decode_pdf).setBackground(new Color(190, 190, 190));
                frame.setBackground(new Color(190, 190, 190));
            }
        }
    }
    
    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void setupComboBoxes() {
        
        
        /**
        * setup scaling, rotation and quality values which are displayed for user to choose
        */
        final String[] qualityValues = new String[2];
        qualityValues[1] = Messages.getMessage("PdfViewerToolbarComboBox.imageQual");
        qualityValues[0] = Messages.getMessage("PdfViewerTooltipComboBox.imageMem");

        final String[] rotationValues={"0","90","180","270"};
    
        final String[] scalingValues = {Messages.getMessage("PdfViewerScaleWindow.text"), Messages.getMessage("PdfViewerScaleHeight.text"),
        Messages.getMessage("PdfViewerScaleWidth.text"),
        "25%", "50%", "75%", "100%", "125%", "150%", "200%", "250%", "500%", "750%", "1000%"};
        
        
        qualityBox = new SwingCombo(qualityValues);

        ((SwingCombo)qualityBox).setBackground(Color.white);
        qualityBox.setSelectedIndex(0); //set default before we add a listener

        //set new default if appropriate
        String choosenScaling = System.getProperty("org.jpedal.defaultViewerScaling");

        //Only use value from properties is VM arguement not set
        if (choosenScaling == null) {
            choosenScaling = properties.getValue("startScaling");
        }

        if (choosenScaling != null) {
            final int total = scalingValues.length;
            for (int aa = 0; aa < total; aa++) {
                if (scalingValues[aa].equals(choosenScaling)) {
                    defaultSelection = aa;
                    aa = total;
                }
            }
        }

        scalingBox = new SwingCombo(scalingValues);
        ((SwingCombo)scalingBox).setBackground(Color.white);
        scalingBox.setEditable(true);
        scalingBox.setSelectedIndex(defaultSelection); //set default before we add a listener

        //if you enable, remember to change rotation and quality Comboboxes
        //scalingBox.setPreferredSize(new Dimension(85,25));
        rotationBox = new SwingCombo(rotationValues);
        ((SwingCombo)rotationBox).setBackground(Color.white);
        rotationBox.setSelectedIndex(0); //set default before we add a listener
    }

    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void setupKeyboardControl() {
        ((PdfDecoder) decode_pdf).addKeyListener(new KeyAdapter() {
            int count;
            int pageChange;
            java.util.Timer t2;

            @Override
            public void keyPressed(final KeyEvent e) {
                final JScrollBar scroll = scrollPane.getVerticalScrollBar();

                final int keyPressed = e.getKeyCode();
                
                if (keyPressed == KeyEvent.VK_LEFT || keyPressed == KeyEvent.VK_RIGHT) {

                    //Only use left and right arrows to change pages if page is smaller than display area
                    if (scrollPane.getWidth() > ((PdfDecoder) decode_pdf).getWidth()) {
                        if (keyPressed == KeyEvent.VK_LEFT) //change page
                        {
                            pageChange--;
                        } else //change page
                        {
                            pageChange++;
                        }

                    }

                } else if ((keyPressed == KeyEvent.VK_UP || keyPressed == KeyEvent.VK_DOWN) && (count == 0)) {
                        if (keyPressed == KeyEvent.VK_UP
                                && scroll.getValue() == scroll.getMinimum()
                                && commonValues.getCurrentPage() > 1) {

                            //change page
                            pageChange--;

                        } else if (keyPressed == KeyEvent.VK_DOWN
                                && (scroll.getValue() == scroll.getMaximum() - scroll.getHeight() || scroll.getHeight() == 0)
                                && commonValues.getCurrentPage() < decode_pdf.getPageCount()) {

                            //change page
                            pageChange++;

                        }
                    }

                count++;

                if (pageChange != 0) {
                    if (t2 != null) {
                        t2.cancel();
                    }

                    final TimerTask t = new TimerTask() {

                        @Override
                        public void run() {

                            int p = (commonValues.getCurrentPage() + pageChange);

                            if (p < 1) {
                                p = 1;
                            } else if (p > decode_pdf.getPageCount()) {
                                p = decode_pdf.getPageCount();
                            }

                            if (p != commonValues.getCurrentPage()) {
                                final String page = String.valueOf(p);

                                currentCommands.executeCommand(Commands.GOTO, new Object[]{page});

                                if (scroll.getValue() == scroll.getMinimum()
                                        && commonValues.getCurrentPage() > 1) {

                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            scroll.setValue(scroll.getMaximum());
                                        }
                                    });

                                } else if ((scroll.getValue() == scroll.getMaximum() - scroll.getHeight() || scroll.getHeight() == 0)
                                        && commonValues.getCurrentPage() < decode_pdf.getPageCount()) {

                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            scroll.setValue(scroll.getMinimum());
                                        }
                                    });
                                }
                            }
                            pageChange = 0;

                    			//Add cancel for the timer at the end of this task.
                            //This should ensure the timer is not kept active
                            if (t2 != null) {
                                t2.cancel();
                            }
                        }
                    };

                    //restart - if its not stopped it will trigger page update
                    t2 = new java.util.Timer();
                    t2.schedule(t, 500);
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                count = 0;
            }
        });
    }
    
    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void setupPDFDisplayPane() {
        if (isSingle) {
            previewOnSingleScroll = properties.getValue("previewOnSingleScroll").toLowerCase().equals("true");
            if (previewOnSingleScroll) {
                thumbscroll = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 1);
                thumbscroll.setName("ThumbnailScroll");
                if (scrollListener == null) {
                    scrollListener = new SwingScrollListener(this);
                }
                thumbscroll.addAdjustmentListener(scrollListener);
                thumbscroll.addMouseListener(scrollListener);
                //thumbscroll.addMouseMotionListener(scrollMouseListener);

                containerForThumbnails.setLayout(new BorderLayout());
                containerForThumbnails.add(thumbscroll, BorderLayout.EAST);
                scrollPane.getViewport().add(((PdfDecoder) decode_pdf));
                containerForThumbnails.add(scrollPane, BorderLayout.CENTER);

                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            } else {
                scrollPane.getViewport().add(((PdfDecoder) decode_pdf));

                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            }

            scrollPane.getVerticalScrollBar().setUnitIncrement(80);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(80);

            //Keyboard control of next/previous page
            setupKeyboardControl();

        }
    }

    /**
     * Overrides method from GUI.java, see GUI.java for DOCS.
     */
    @Override
    protected void setupBorderPanes() {
        if (isSingle) {
            /**
             * Create a left-right split pane with tabs and add to main display
             */
            navOptionsPanel.setTabPlacement(JTabbedPane.LEFT);
            navOptionsPanel.setOpaque(true);
            //Use start size as min width to keep divider from covering tabs
            navOptionsPanel.setMinimumSize(new Dimension(collapsedSize, 100));
            navOptionsPanel.setName("NavPanel");
            navOptionsPanel.setFocusable(false);
            
            setupSidebarTitles();
            
            if (previewOnSingleScroll) {
                displayPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navOptionsPanel, containerForThumbnails);
            } else {
                displayPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navOptionsPanel, scrollPane);
            }

            displayPane.setOneTouchExpandable(false);

            //update scaling when divider moved
            displayPane.addPropertyChangeListener("dividerLocation", new PropertyChangeListener() {
                @Override
                public void propertyChange(final PropertyChangeEvent e) {

                    //hack to get it to use current values instead of old values
                    scrollPane.getViewport().setSize((scrollPane.getViewport().getWidth() + (Integer) e.getOldValue() - (Integer) e.getNewValue()),
                            scrollPane.getViewport().getHeight());

                    desktopPane.setSize((desktopPane.getWidth() + (Integer) e.getOldValue() - (Integer) e.getNewValue()),
                            desktopPane.getHeight());

                    //Keep track of the current divider position
                    final int dividerPos = (Integer) e.getNewValue();

                    if (tabsExpanded && (dividerPos > collapsedSize)) {
                        expandedSize = dividerPos;
                    }

                    scaleAndRotate();

                }
            });

            if (DecoderOptions.isRunningOnMac) {
                navOptionsPanel.addTab(pageTitle, (Component) thumbnails);
                navOptionsPanel.setTitleAt(navOptionsPanel.getTabCount() - 1, pageTitle);

                if (thumbnails.isShownOnscreen()) {
                    navOptionsPanel.addTab(bookmarksTitle, (SwingOutline) tree);
                    navOptionsPanel.setTitleAt(navOptionsPanel.getTabCount() - 1, bookmarksTitle);
                }

            } else {

                //Remove borders from thumbnail and bookmark tab in windows as looks better
                ((SwingOutline) tree).setBorder(null);
                ((SwingThumbnailPanel) thumbnails).setBorder(null);

                if (thumbnails.isShownOnscreen()) {
                    final VTextIcon textIcon1 = new VTextIcon(navOptionsPanel, pageTitle, VTextIcon.ROTATE_LEFT);
                    navOptionsPanel.addTab(null, textIcon1, (Component) thumbnails);

                    //navOptionsPanel.setTitleAt(navOptionsPanel.getTabCount()-1, pageTitle);
                }

                final VTextIcon textIcon2 = new VTextIcon(navOptionsPanel, bookmarksTitle, VTextIcon.ROTATE_LEFT);
                navOptionsPanel.addTab(null, textIcon2, (SwingOutline) tree);
                //navOptionsPanel.setTitleAt(navOptionsPanel.getTabCount()-1, bookmarksTitle);

            }

            //				p.setTabDefaults(defaultValues);
//            displayPane.setDividerLocation(startSize);
            propValue = properties.getValue("startSideTabOpen");
            if (!propValue.isEmpty()) {
                sideTabBarOpenByDefault = propValue.toLowerCase().equals("true");
            }

            propValue = properties.getValue("startSelectedSideTab");
            if (!propValue.isEmpty()) {
                startSelectedTab = propValue;
            }

            if (!hasListener) {
                hasListener = true;
                navOptionsPanel.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(final MouseEvent mouseEvent) {
                        handleTabbedPanes();
                    }

                    @Override
                    public void mousePressed(final MouseEvent mouseEvent) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void mouseReleased(final MouseEvent mouseEvent) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void mouseEntered(final MouseEvent mouseEvent) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void mouseExited(final MouseEvent mouseEvent) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                });

            }

        }
    }
    
    
    /**
     * Sets up the position & visual style of the items on the bottom toolbar
     * (page navigation buttons etc).
     */
    @Override
    protected void setupBottomToolBarItems() {

        /**
         * set colours on display boxes and add listener to page number
         */
        pageCounter2.setEditable(true);
        pageCounter2.setToolTipText(Messages.getMessage("PdfViewerTooltip.goto"));
//		pageCounter2.setBorder(BorderFactory.createLineBorder(Color.black));
        pageCounter2.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1),
                pageCounter2.getBorder()));
        pageCounter2.setColumns(2);
        pageCounter2.setMaximumSize(pageCounter2.getPreferredSize());

        pageCounter2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {

                final String value = pageCounter2.getText().trim();

                currentCommands.executeCommand(Commands.GOTO, new Object[]{value});
            }

        });
        pageCounter2.setHorizontalAlignment(JTextField.CENTER);
        pageCounter2.setForeground(Color.black);
        setPageNumber();

        pageCounter3 = new JLabel(Messages.getMessage("PdfViewerOfLabel.text") + ' ');
        pageCounter3.setOpaque(false);

        /**
         * nav bar at bottom to select pages and setup Toolbar on it
         */
        //navToolBar.setLayout(new FlowLayout());
        navToolBar.setLayout(new BoxLayout(navToolBar, BoxLayout.LINE_AXIS));
        navToolBar.setFloatable(false);

        //pagesToolBar.setLayout(new FlowLayout());
        pagesToolBar.setFloatable(false);

        navButtons.setBorder(BorderFactory.createEmptyBorder());
        navButtons.setLayout(new BorderLayout());
        navButtons.setFloatable(false);
//		comboBar.setFont(new Font("SansSerif", Font.PLAIN, 8));
        navButtons.setPreferredSize(new Dimension(5, 24));

    }
  
    /**
     * Creates a glowing border around the PDFDisplayPane.
     */
    @Override
    protected void setupPDFBorder() {
        //add a border
        if (decode_pdf.useNewGraphicsMode()) {
            decode_pdf.setPDFBorder(new AbstractBorder() {

                @Override
                public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {
                    final Graphics2D g2 = (Graphics2D) g;

                    final int cornerDepth = (glowThickness / 2) + 1;

                    //left
                    g2.setPaint(new GradientPaint(x, 0, glowOuterColor, x + glowThickness, 0, glowInnerColor));
                    g2.fillRect(x, y + glowThickness, glowThickness, height - (glowThickness * 2));

                    //bottom left corner
                    g2.setPaint(new GradientPaint(x - cornerDepth + glowThickness, y + height + cornerDepth - glowThickness, glowOuterColor, x + glowThickness, y + height - glowThickness, glowInnerColor));
                    g2.fillRect(x, y + height - glowThickness, glowThickness, glowThickness);

                    //below
                    g2.setPaint(new GradientPaint(0, y + height, glowOuterColor, 0, y + height - glowThickness, glowInnerColor));
                    g2.fillRect(x + glowThickness, y + height - glowThickness, width - (glowThickness * 2), glowThickness);

                    //bottom right corner
                    g2.setPaint(new GradientPaint(x + width + cornerDepth - glowThickness, y + height + cornerDepth - glowThickness, glowOuterColor, x + width - glowThickness, y + height - glowThickness, glowInnerColor));
                    g2.fillRect(x + width - glowThickness, y + height - glowThickness, glowThickness, glowThickness);

                    //right
                    g2.setPaint(new GradientPaint(x + width, 0, glowOuterColor, x + width - glowThickness, 0, glowInnerColor));
                    g2.fillRect(x + width - glowThickness, y + glowThickness, glowThickness, height - (glowThickness * 2));

                    //top right corner
                    g2.setPaint(new GradientPaint(x + width - glowThickness + cornerDepth, y + glowThickness - cornerDepth, glowOuterColor, x + width - glowThickness, y + glowThickness, glowInnerColor));
                    g2.fillRect(x + width - glowThickness, y, glowThickness, glowThickness);

                    //above
                    g2.setPaint(new GradientPaint(0, y, glowOuterColor, 0, y + glowThickness, glowInnerColor));
                    g2.fillRect(x + glowThickness, y, width - (glowThickness * 2), glowThickness);

                    //top left corner
                    g2.setPaint(new GradientPaint(x - cornerDepth + glowThickness, y - cornerDepth + glowThickness, glowOuterColor, x + glowThickness, y + glowThickness, glowInnerColor));
                    g2.fillRect(x, y, glowThickness, glowThickness);

                    //draw black over top
                    g2.setPaint(Color.black);
                    g2.drawRect(x + glowThickness, y + glowThickness, width - (glowThickness * 2), height - (glowThickness * 2));

                }

                @Override
                public Insets getBorderInsets(final Component c, final Insets insets) {
                    insets.set(glowThickness, glowThickness, glowThickness, glowThickness);
                    return insets;
                }
            });

        } else {
            decode_pdf.setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
        }

    }
    
    /**
     * Creates the top two menu bars, the file loading & viewer properties one
     * and the PDF toolbar, the one which controls printing, searching etc.
     */
    @Override
    protected void createTopMenuBar(){
        top.setLayout(new BorderLayout());
        if (frame instanceof JFrame) {
            ((JFrame) frame).getContentPane().add(top, BorderLayout.NORTH);
        } else {
            frame.add(top, BorderLayout.NORTH);
        }
    }

    /**
     * Ensure Document is redrawn when frame is resized and scaling set to
     * width, height or window.
     */
    private void redrawDocumentOnResize() {
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentHidden(final ComponentEvent e) {
            }

            @Override
            public void componentMoved(final ComponentEvent e) {
            }

            @Override
            public void componentResized(final ComponentEvent e) {
				if(((PdfDecoder)decode_pdf).getParent()!=null &&
                        (getSelectedComboIndex(Commands.SCALING)<3 || decode_pdf.getDisplayView() == Display.FACING)) //always rezoom in facing mode for turnover
                {
                    scaleAndRotate();
                }
            }

            @Override
            public void componentShown(final ComponentEvent e) {
            }
        });
    }
    
    /**
     * Creates the Main Display Window for all of the Swing Content.
     *
     * @param width is of type int
     * @param height is of type int
     */
    @Override
    protected void createMainViewerWindow(final int width, final int height) {
        if (frame instanceof JFrame) {
            frame.setSize(width, height);
            ((JFrame) frame).setLocationRelativeTo(null); //centre on screen
            ((JFrame) frame).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            ((JFrame) frame).addWindowListener(new FrameCloser(currentCommands, this, decode_pdf, thumbnails, commonValues, properties));
            frame.setVisible(true);
        }
    }
    
    @Override
    public RecentDocumentsFactory getRecentDocument(){
        if(recent!=null){
         return recent;
        }
        else{
            return recent = new RecentDocuments(PropertiesFile.getNoRecentDocumentsToDisplay());
        }
    }
    
    @Override
    public void setRecentDocument(){
        recent = new RecentDocuments(PropertiesFile.getNoRecentDocumentsToDisplay());
    }
    
    @Override
    public void openFile(final String fileToOpen) {
        if (fileToOpen != null) {  
            OpenFile.open(fileToOpen, commonValues, searchFrame, this, decode_pdf, properties, thumbnails);
        }
    }
    
    @Override
    public void open(final String fileName){
            OpenFile.open(fileName, commonValues, searchFrame, this, decode_pdf, properties, thumbnails);
    }

    @Override
    public void enablePageCounter(final PageCounter value, final boolean enabled, final boolean visibility) {
        switch (value) {

            case PAGECOUNTER1:
                pageCounter1.setVisible(visibility);
                pageCounter1.setEnabled(enabled);
                break;

            case PAGECOUNTER2:
                pageCounter2.setVisible(visibility);
                pageCounter2.setEnabled(enabled);
                break;

            case PAGECOUNTER3:
                pageCounter3.setVisible(visibility);
                pageCounter3.setEnabled(enabled);
                break;
                
            case ALL:
                pageCounter1.setVisible(visibility);
                pageCounter1.setEnabled(enabled);
                pageCounter2.setVisible(visibility);
                pageCounter2.setEnabled(enabled);
                pageCounter3.setVisible(visibility);
                pageCounter3.setEnabled(enabled);
                break;

            default:
                System.out.println("No Value detected, please choose from Enum PageCounter in GUI.java");
                break;
        }
    }

    @Override
    public void setPageCounterText(final PageCounter value, final String text) {
        switch (value) {

            case PAGECOUNTER1:
                pageCounter1.setText(text);
                break;

            case PAGECOUNTER2:
                pageCounter2.setText(text);
                break;

            case PAGECOUNTER3:
                pageCounter3.setText(text);
                break;

            default:
                System.out.println("No Value detected, please choose from Enum PageCounter in GUI.java");
                break;
        }
    }
    
    @Override
    public Object getPageCounter(final PageCounter value){
        switch (value) {

            case PAGECOUNTER1:                
                return pageCounter1;

            case PAGECOUNTER2:
                return pageCounter2;

            case PAGECOUNTER3:
                return pageCounter3;

            default:
                System.out.println("No Value detected, please choose from Enum PageCounter in GUI.java");
                return 0;
        }
    }
    
    @Override
    public void updateTextBoxSize(){
        //Set textbox size
        int col = (String.valueOf(commonValues.getPageCount())).length();
        if (decode_pdf.getDisplayView() == Display.FACING || decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING) {
            col *= 2;
        }
        if (col < 2) {
            col = 2;
        }
        if (col > 10) {
            col = 10;
        }
        pageCounter2.setColumns(col);
        pageCounter2.setMaximumSize(pageCounter2.getPreferredSize());
        
        navToolBar.invalidate();
        navToolBar.doLayout();
        
    }
    
    @Override
    public void enableCursor(final boolean enabled, final boolean visible){
        cursor.setEnabled(enabled);
        cursor.setVisible(visible);
    }

    @Override
    public void enableMemoryBar(final boolean enabled, final boolean visible){
        memoryBar.setEnabled(enabled);
        memoryBar.setVisible(visible);
    }
    
    @Override
    public void enableNavigationBar(final boolean enabled, final boolean visible){
        navButtons.setEnabled(enabled);
        navButtons.setVisible(enabled);
    }
    
    @Override
    public void enableDownloadBar(final boolean enabled, final boolean visible){
        downloadBar.setEnabled(enabled);
        downloadBar.setVisible(visible);
    }

    @Override
    public int getSidebarTabCount() {
        return navOptionsPanel.getTabCount();
    }

    @Override
    public String getSidebarTabTitleAt(final int pos) {
        if(navOptionsPanel.getIconAt(pos)!=null) {
            return navOptionsPanel.getIconAt(pos).toString();
        } else {
            return "";
        }
    }
    
    @Override
    public void removeSidebarTabAt(final int pos) {
        navOptionsPanel.remove(pos);
    }

    @Override
    public double getDividerLocation() {
        return displayPane.getDividerLocation();
    }
    
    /**
     * calculates the scaling required to make the given area visible
     */
    @Override
    public float scaleToVisible(final float left, final float right, final float top, final float bottom){

    	float scaling;

    	float width;
        final float height;

        if(isSingle){

    		width = scrollPane.getViewport().getWidth()-inset-inset;
    		height = scrollPane.getViewport().getHeight()-inset-inset;

    	}else{
    		width=desktopPane.getSelectedFrame().getWidth();
    		height=desktopPane.getSelectedFrame().getHeight();
    	}
    	if(isSingle && displayPane!=null) {
                width -= displayPane.getDividerSize();
            }

    	final float widthScaling = (right-left)/width;
    	final float heightScaling = (top-bottom)/height;
    	
    	if(widthScaling>heightScaling) {
            scaling = widthScaling;
        } else {
            scaling = heightScaling;
        }
    	
    	scaling = decode_pdf.getDPIFactory().adjustScaling(scaling);
    	
    	return scaling;
    }
    
    @Override
    public int getDropShadowDepth(){
        return glowThickness;
    }
    
    @Override
    public void setPannable(final boolean pan){//stub
    }

    @Override
    public void setupSplitPaneDivider(final int size, final boolean visibility) {
        displayPane.setDividerSize(size);
        displayPane.getLeftComponent().setEnabled(visibility);
        displayPane.getLeftComponent().setVisible(visibility);
    }
    
    @Override
    public double getStartSize(){
        return collapsedSize;
    }
    
    @Override
    public void setStartSize(final int size){
        collapsedSize = size;
    }
 
    @Override
    public JScrollPane getPageContainer(){
        return scrollPane;
    }
    
    @Override
    public void enableStatusBar(final boolean enabled, final boolean visible){
        statusBar.setEnabled(enabled);
        statusBar.setVisible(enabled);
    }
    

    private Point getPageCoordsInSingleDisplayMode(int x, int y, final int page){
        //<start-adobe>
            final int[] flag = new int[2];
            
            flag[0] = SwingGUI.CURSOR;
            flag[1]=0;
            
            final int pageWidth;
        final int pageHeight;
        if (getRotation()%180==90) {
                pageWidth = decode_pdf.getPdfPageData().getScaledCropBoxHeight(page);
                pageHeight = decode_pdf.getPdfPageData().getScaledCropBoxWidth(page);
            } else {
                pageWidth = decode_pdf.getPdfPageData().getScaledCropBoxWidth(page);
                pageHeight = decode_pdf.getPdfPageData().getScaledCropBoxHeight(page);
            }
            
            final Rectangle pageArea = new Rectangle(
                    (((PdfDecoder)decode_pdf).getWidth()/2) - (pageWidth/2),
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
//            if(pageOfHighlight==-1 && startHighlighting){
//                pageOfHighlight = page;
//            }
            
            //Keep track of page the mouse is over at all times
//            pageMouseIsOver = page;
            
            setMultibox(flag);
        
        //<end-adobe>
        
        final float scaling=getScaling();
        final int inset= GUI.getPDFDisplayInset();
        final int rotation=getRotation();
        
        
        //Apply inset to values
        int ex=adjustForAlignment(x,decode_pdf)-inset;
        int ey=y-inset;
        
        final PdfPageData page_data = decode_pdf.getPdfPageData();
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
            final int originalPage = page;
            flag[0] = SwingGUI.CURSOR;
            flag[1]=0;
            
            //In continuous pages are centred so we need make
            int xAdjustment = (((PdfDecoder)decode_pdf).getWidth()/2) - (decode_pdf.getPdfPageData().getScaledCropBoxWidth(page)/2);
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
            
                //Not within area so just get coordinates in relation to page passed in
                if(flag[1]==0){
                	page = originalPage;
                }
                
            //Set highlighting page
//            if(pageOfHighlight==-1 && startHighlighting){
//                pageOfHighlight = page;
//            }
            
            //Keep track of page mouse is over at all times
//            pageMouseIsOver = page;
            
        }
            
        //Tidy coords for multipage views
        y= ((pages.getYCordForPage(page)+decode_pdf.getPdfPageData().getScaledCropBoxHeight(page))+decode_pdf.getInsetH())-y;
            
        setMultibox(flag);

        //<end-adobe>
        
        final float scaling=getScaling();
        final int inset= GUI.getPDFDisplayInset();
        final int rotation=getRotation();
        
        //Apply inset to values
        int ex=adjustForAlignment(x, decode_pdf)-inset;
        int ey=y-inset;
        
        final PdfPageData page_data = decode_pdf.getPdfPageData();
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
            final int originalPage = page;
            
            flag[0] = SwingGUI.CURSOR;
            flag[1]=0;
            
            //Check if we are in the region of the left or right pages
            if(page != 1 && x>(((PdfDecoder)decode_pdf).getWidth()/2) && page<commonValues.getPageCount()){// && x>pageArea.x){
                page++;
            }
            
            //Set the adjustment for page position
            int xAdjustment = (((PdfDecoder)decode_pdf).getWidth()/2) - (decode_pdf.getPdfPageData().getScaledCropBoxWidth(page))-(decode_pdf.getInsetW());
            
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
                        xAdjustment = (((PdfDecoder)decode_pdf).getWidth()/2) - (decode_pdf.getPdfPageData().getScaledCropBoxWidth(page))-(decode_pdf.getInsetW());
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
                            xAdjustment = (((PdfDecoder)decode_pdf).getWidth()/2) - (decode_pdf.getPdfPageData().getScaledCropBoxWidth(page))-(decode_pdf.getInsetW());
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
              //Not within area so just get coordinates in relation to page passed in
                if(flag[1]==0){
                	page = originalPage;
                }
            }
            
            //Set highlighting page
//            if(pageOfHighlight==-1 && startHighlighting){
//                pageOfHighlight = page;
//            }
            
            //Keep track of page mouse is over at all times
//            pageMouseIsOver = page;
            
            //Tidy coords for multipage views
            y= (((pages.getYCordForPage(page)+decode_pdf.getPdfPageData().getScaledCropBoxHeight(page))+decode_pdf.getInsetH()))-y;
            
            x -= ((pages.getXCordForPage(page))-decode_pdf.getInsetW());
            
            setMultibox(flag);
            
        
        //<end-adobe>
        
        
        final float scaling=getScaling();
        final int inset= GUI.getPDFDisplayInset();
        final int rotation=getRotation();
        
        
        //Apply inset to values
        int ex=adjustForAlignment(x,decode_pdf)-inset;
        int ey=y-inset;
        final PdfPageData page_data = decode_pdf.getPdfPageData();
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
        if (getRotation()%180==90) {
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
            double left = (((PdfDecoder)decode_pdf).getWidth()/2) - (pageWidth/2);
            double right = (((PdfDecoder)decode_pdf).getWidth()/2) + (pageWidth/2);
            
            if(decode_pdf.getDisplayView()==Display.FACING){
            	 left = (((PdfDecoder)decode_pdf).getWidth()/2);
            	 if(decode_pdf.getPageNumber()!=1 || decode_pdf.getPageCount()==2) {
                     left -= (pageWidth);
                 }
            	 
                 right = (((PdfDecoder)decode_pdf).getWidth()/2) + (pageWidth);
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
        
        setMultibox(flag);
    
        //<end-adobe>
        
        scaling=getScaling();
        final int inset= GUI.getPDFDisplayInset();
        final int rotation=getRotation();
        
        
        //Apply inset to values
        int ex=adjustForAlignment(x,decode_pdf)-inset;
        int ey=y-inset;
        
        final PdfPageData page_data = decode_pdf.getPdfPageData();
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
     * Convertes mouse / components coords to pdf page coords
     * @param x :: The x coordinate of the cursors location in display area coordinates
     * @param y :: The y coordinate of the cursors location in display area coordinates
     * @param page :: The page we are currently on
     * @return Point object of the cursor location in page coordinates
     */
    public Point convertComponentCoordsToPageCoords(int x, int y, final int page){
        
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
        
    /**
     * adjusty x co-ordinate shown in display for user to include any page
     * centering
     */
    private static int adjustForAlignment(int cx, final PdfDecoderInt decode_pdf) {

        if (decode_pdf.getPageAlignment() == Display.DISPLAY_CENTERED) {
            final int width =  decode_pdf.getPaneBounds()[0];
            int pdfWidth = decode_pdf.getPDFWidth();

            if (decode_pdf.getDisplayView() != Display.SINGLE_PAGE) {
                pdfWidth = decode_pdf.getMaxSizeWH()[0];
            }

            if (width > pdfWidth) {
                cx -= ((width - pdfWidth) / (2));
            }
        }

        return cx;
    }
}