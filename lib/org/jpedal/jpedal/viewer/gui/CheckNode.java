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
 * CheckNode.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class CheckNode extends DefaultMutableTreeNode {

    protected boolean isSelected, isEnabled;
    private Object text;

    public CheckNode(final Object userObject) {
        this(userObject, true, false);

        text=userObject;
    }

    public CheckNode(final Object userObject, final boolean allowsChildren
            , final boolean isSelected) {
        super(userObject, allowsChildren);
        this.isSelected = isSelected;

        text=userObject;
    }

    public Object getText() {
        return text;
    }

    public void setSelected(final boolean isSelected) {
        this.isSelected = isSelected;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setEnabled(final boolean isEnabled) {
        this.isEnabled = isEnabled;

    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
