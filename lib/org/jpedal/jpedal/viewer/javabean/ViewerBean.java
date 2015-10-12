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
 * ViewerBean.java
 * ---------------
 */

package org.jpedal.examples.viewer.javabean;

import java.io.File;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.Values;


public class ViewerBean extends JPanel {
	private final Viewer viewer;
	
	private File document;
	private Integer pageNumber;
	private Integer rotation;
	private Integer zoom;

	private Boolean isMenuBarVisible;
	private Boolean isToolBarVisible;
	private Boolean isDisplayOptionsBarVisible;
	private Boolean isSideTabBarVisible;
	private Boolean isNavigationBarVisible;
	
	ViewerBean() {
        viewer = new Viewer(this, Viewer.PREFERENCES_BEAN);
        viewer.setupViewer();
	}
	
    public Viewer getViewer() {
    	return viewer;
    }
    
    // Document ////////
	public void setDocument(final File document) {
		this.document = document;
		
		excuteCommand(Commands.OPENFILE, new String[] { 
				String.valueOf(document) });
		
		if(pageNumber != null) {
			excuteCommand(Commands.GOTO, new String[] { 
				String.valueOf(pageNumber) });
		}
		
		if(rotation != null) {
			excuteCommand(Commands.ROTATION, new String[] { 
				String.valueOf(rotation) });
		}
		
		if(zoom != null) {
			excuteCommand(Commands.SCALING, new String[] { 
				String.valueOf(zoom) });
		} else {
			excuteCommand(Commands.SCALING, new String[] { 
					String.valueOf(100) });
		}
		
		if(isMenuBarVisible != null) {
			setMenuBar(isMenuBarVisible);
		}
		
		if(isToolBarVisible != null) {
			setToolBar(isToolBarVisible);
		}
		
		if(isDisplayOptionsBarVisible != null) {
			setDisplayOptionsBar(isDisplayOptionsBarVisible);
		}
		
		if(isSideTabBarVisible != null) {
			setSideTabBar(isSideTabBarVisible);
		}
		
		if(isNavigationBarVisible != null) {
			setNavigationBar(isNavigationBarVisible);
		}
	}
	
	// Page Number ////////
	public int getPageNumber() {
		if(pageNumber == null) {
            return 1;
        } else {
            return pageNumber;
        }
	}
	
	public void setPageNumber(final int pageNumber) {
		this.pageNumber = pageNumber;
		
		if(document != null) {
			excuteCommand(Commands.GOTO, new String[] { 
				String.valueOf(pageNumber) });
		}
	}

	// Rotation ////////
	public int getRotation() {
		if(rotation == null) {
            return 0;
        } else {
            return rotation;
        }
	}

	public void setRotation(final int rotation) {
		this.rotation = rotation;

		if(document != null) {
			excuteCommand(Commands.ROTATION, new String[] { 
				String.valueOf(rotation) });
		}
	}
	
	// Zoom ////////
	public int getZoom() {
		if(zoom == null) {
            return 100;
        } else {
            return zoom;
        }
	}

	public void setZoom(final int zoom) {
		this.zoom = zoom;
		
		if(document != null) {
			excuteCommand(Commands.SCALING, new String[] { 
				String.valueOf(zoom) });
		}
	}

	//setToolBar, setDisplayOptionsBar, setSideTabBar, setNavigationBar, 
	public void setMenuBar(final boolean visible) {
		this.isMenuBarVisible = visible;
		
		//if(document != null)
			viewer.executeCommand(Commands.UPDATEGUILAYOUT, new Object[] {"ShowMenubar", visible});
	}
	
	public boolean getMenuBar() {
		if(isMenuBarVisible == null) {
            return true;
        } else {
            return isMenuBarVisible;
        }
	}
	
	public void setToolBar(final boolean visible) {
		this.isToolBarVisible = visible;
		
		viewer.executeCommand(Commands.UPDATEGUILAYOUT, new Object[] {"ShowButtons", visible});
	}
	
	public boolean getToolBar() {
		if(isToolBarVisible == null) {
            return true;
        } else {
            return isToolBarVisible;
        }
	}
	
	public void setDisplayOptionsBar(final boolean visible) {
		this.isDisplayOptionsBarVisible = visible;
		
		//if(document != null)
			viewer.executeCommand(Commands.UPDATEGUILAYOUT, new Object[] {"ShowDisplayoptions", visible});
	}
	
	public boolean getDisplayOptionsBar() {
		if(isDisplayOptionsBarVisible == null) {
            return true;
        } else {
            return isDisplayOptionsBarVisible;
        }
	}
	
	public void setSideTabBar(final boolean visible) {
		this.isSideTabBarVisible = visible;
		
		//if(document != null)
			viewer.executeCommand(Commands.UPDATEGUILAYOUT, new Object[] {"ShowSidetabbar", visible});
	}
	
	public boolean getSideTabBar() {
		if(isSideTabBarVisible == null) {
            return true;
        } else {
            return isSideTabBarVisible;
        }
	}
	
	public void setNavigationBar(final boolean visible) {
		this.isNavigationBarVisible = visible;
		
		//if(document != null)
			viewer.executeCommand(Commands.UPDATEGUILAYOUT, new Object[] {"ShowNavigationbar", visible});
	}
	
	public boolean getNavigationBar() {
		if(isNavigationBarVisible == null) {
            return true;
        } else {
            return isNavigationBarVisible;
        }
	}
	
	private void excuteCommand(final int command, final Object[] input) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
            public void run() {
				viewer.executeCommand(command, input);
				
				while(Values.isProcessing()) {
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				repaint();
			}
		});
	}
	
//	// Page Layout ////////
//	private String pageLayout = "Single";
//	
//	public String getPageLayout() {
//		return pageLayout;
//	}
//
//	public void setPageLayout(String pageLayout) {
//		this.pageLayout = pageLayout;
//	}
}