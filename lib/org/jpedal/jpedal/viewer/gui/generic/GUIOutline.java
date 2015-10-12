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
 * GUIOutline.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.generic;

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Node;

/**abstract level for outlines panel*/
public interface GUIOutline {

	public Object getTree();

	public DefaultMutableTreeNode getLastSelectedPathComponent();

	public String getPage(String title);

	//Point getPoint(String title);

	//void setMinimumSize(Dimension dimension);

	public void selectBookmark();

	//int readChildNodes(Node rootNode,DefaultMutableTreeNode topNode, int nodeIndex);

    public void reset(Node rootNode);

	//String getPageViaNodeNumber(int nodeNumber);

    public String convertNodeIDToRef(int index);
}
