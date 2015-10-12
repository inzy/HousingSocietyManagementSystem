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
 * SwingOutline.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.jpedal.examples.viewer.gui.generic.GUIOutline;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**holds tree outline displayed in nav bar*/
public class SwingOutline extends JScrollPane implements GUIOutline{

	/**used by tree to convert page title into page number*/
	private final Map pageLookupTableViaTitle=new HashMap();
	
	//private Map pageLookupTableViaNodeNumber=new HashMap();
	private final Map nodeToRef=new HashMap();

    //list of closed nodes so we display closed at start
    private final Map closedNodes=new HashMap();

	/**used by tree to find point to scroll to*/
	//private Map pointLookupTable=new HashMap();
	
	private final DefaultMutableTreeNode top =new DefaultMutableTreeNode("Root"); //$NON-NLS-1$
	
	private JTree tree;

	private boolean hasDuplicateTitles;

	
	/**specify bookmark for each page*/
	//private String[] defaultRefsForPage;
	
	//private TreeNode[] defaultPageLookup;

    public SwingOutline() {
        this.getViewport().add(new JLabel("No outline"));
    }

    @Override
    public void reset(final Node rootNode) {

        top.removeAllChildren();
        if(tree!=null) {
            getViewport().remove(tree);
        }

	    pageLookupTableViaTitle.clear();

	    nodeToRef.clear();

        closedNodes.clear();

	    hasDuplicateTitles=false;


        /**
		 * default settings for bookmarks for each page
		 */
		//defaultRefsForPage=decode_pdf.getOutlineDefaultReferences();
		//this.defaultPageLookup=new TreeNode[this.pageCount];

        if(rootNode!=null){
        	hasDuplicateTitles=false;
        	readChildNodes(rootNode,top, 0);
        }
		tree=new JTree(top);
		tree.setName("Tree");

        if(rootNode!=null) {
            expandAll();
        }
		
		tree.setRootVisible(false);
		
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		//create display for bookmarks
		getViewport().add(tree);

		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    }
	
	/**
	 * Expand all nodes found from the XML outlines for the PDF.
	 */
	private void expandAll() {
		int row = 1;
        //while (row < tree.getRowCount()) {
		while (row < 4) {

            if(!closedNodes.containsKey(row)) {
                tree.expandRow(row);
            } else {
                tree.collapseRow(row);
            }

			row++;
		}
	}
	
	/**
	 * Scans sublist to get the children bookmark nodes.
	 *
	 * @param rootNode Node
	 * @param topNode DefaultMutableTreeNode
	 */
    public int readChildNodes(final Node rootNode,DefaultMutableTreeNode topNode, int nodeIndex) {

		if (topNode == null) {
            topNode = top;
        }

		final NodeList children = rootNode.getChildNodes();
		final int childCount = children.getLength();
		
		for (int i = 0; i < childCount; i++) {

			final Node child = children.item(i);

			final Element currentElement = (Element) child;

			final String title = currentElement.getAttribute("title");
			final String page = currentElement.getAttribute("page");
			final String isClosed = currentElement.getAttribute("isClosed");
            final String ref=currentElement.getAttribute("objectRef");

            // String ref=currentElement.getAttribute("objectRef");

			/**create the lookup table*/

			if (pageLookupTableViaTitle.containsKey(title)) {
				hasDuplicateTitles = true;
			} else {
				pageLookupTableViaTitle.put(title, page);
			}

            if(isClosed.equals("true")) {
                closedNodes.put(nodeIndex, "x");
            }


            //build lookup tables so we can cross-ref against tree in viewer
			//pageLookupTableViaNodeNumber.put(new Integer(nodeIndex), page);
			nodeToRef.put(nodeIndex, ref);

			nodeIndex++;
			
			/** create the point lookup table */
//			if ((rawDest != null) && (rawDest.indexOf("/XYZ") != -1)) {
//
//				rawDest = rawDest.substring(rawDest.indexOf("/XYZ") + 4);
//
//				StringTokenizer values = new StringTokenizer(rawDest, "[] ");
//
//				//ignore the first, read next 2
//
//				String x = values.nextToken();
//				if (x.equals("null"))
//					x = "0";
//				String y = values.nextToken();
//				if (y.equals("null"))
//					y = "0";
//
//				pointLookupTable.put(title, new Point((int) Float.parseFloat(x), (int) Float.parseFloat(y)));
//			}
			
			
			final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(title);

			/** add the nodes or initialise to top level */
			topNode.add(childNode);


			if (child.hasChildNodes()) {
                nodeIndex = readChildNodes(child, childNode, nodeIndex);
            }
			
		}
		
		return nodeIndex;
	}
	
	@Override
    public String getPage(final String title) {
		if(hasDuplicateTitles) {
            return null;
        }//throw new RuntimeException("Bookmark "+title+" not unique");
		else {
            return (String) pageLookupTableViaTitle.get(title);
        }
	}

    /**
	public String getPageViaNodeNumber(int nodeNumber){
		return (String) pageLookupTableViaNodeNumber.get(new Integer(nodeNumber));
	}/**/

    @Override
    public String convertNodeIDToRef(final int nodeNumber){
		return (String) nodeToRef.get(nodeNumber);
	}
	
	/**
	 * Handles the functionality for highlighting the correct bookmark
	 * tree node for the page we opened the PDF to.
	 */
	@Override
    public void selectBookmark() {
		
		
		/** code to walk not fully operational so only runs on example
	     
	      traverse();
	     
	        try{
	        System.out.println(defaultPageLookup[this.currentPage-1]);
	        ignoreAlteredBookmark=true;
	      tree.setSelectionPath(new TreePath(defaultPageLookup[this.currentPage]));
	      ignoreAlteredBookmark=false;
	      System.out.println(tree.getSelectionPath()+" "+currentPage+" "+defaultPageLookup[this.currentPage-1]);
	        }catch(Exception e){
	            e.printStackTrace();
	            System.exit(1);
	        }/***/
		
	}
	
//	public Point getPoint(String title) {
//
//		return (Point) pointLookupTable.get(title);
//	}
	
	@Override
    public Object getTree() {
		
		return tree;
	}
	
	@Override
    public DefaultMutableTreeNode getLastSelectedPathComponent() {
		
		return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
	}
	
}
