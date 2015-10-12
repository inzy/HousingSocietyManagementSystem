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
 * Viewable.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Rectangle;

/**
 * a view of a defined page, Rectangle on page, and scalingtype
 */
public class Viewable {

    private final int page;
    private final Rectangle location;
    private final Integer type;

    public Viewable(final int inPage, final Rectangle rectangle, final Integer inType) {
        page = inPage;
        location = rectangle;
        type = inType;
    }

    public Rectangle getLocation() {
        return location;
    }

    public int getPage() {
        return page;
    }

    public Integer getType() {
        return type;
    }
}
