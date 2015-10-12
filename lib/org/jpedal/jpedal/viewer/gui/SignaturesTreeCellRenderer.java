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
 * SignaturesTreeCellRenderer.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SignaturesTreeCellRenderer extends DefaultTreeCellRenderer {
    private Icon icon;

    @Override
    public Icon getLeafIcon() {
        return icon;
    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, Object value, final boolean isSelected,
                                                  final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {

        final DefaultMutableTreeNode node = ((DefaultMutableTreeNode) value);
		value = node.getUserObject();
		final int level = node.getLevel();
		
        final String s = value.toString();
        icon = null;
        Font treeFont = tree.getFont();

        if(level== 2){
        	final DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        	final String text=parent.getUserObject().toString();
        	if(text.equals("The following signature fields are not signed")){
        		final URL resource = getClass().getResource("/org/jpedal/examples/viewer/res/unlock.png");
        		icon = new ImageIcon(resource);
        	} else {
        		final URL resource = getClass().getResource("/org/jpedal/examples/viewer/res/lock.gif");
        		icon = new ImageIcon(resource);
        		treeFont = new Font(treeFont.getFamily(), Font.BOLD, treeFont.getSize());
        	}
        }
        
        setFont(treeFont);
        setText(s);
        setIcon(icon);
        if (isSelected) {
            setBackground(new Color(236, 233, 216));
            setForeground(Color.BLACK);
        } else {
            setBackground(tree.getBackground());
            setForeground(tree.getForeground());
        }
        setEnabled(tree.isEnabled());
        
        setOpaque(true);

        return this;
    }
}
