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
 * MarginPaper.java
 * ---------------
 */

package org.jpedal.examples.viewer.paper;

import java.awt.print.Paper;

/**
 * Created by IntelliJ IDEA.
 * User: Sam
 * Date: 02-Jul-2010
 * Time: 16:41:28
 * To change this template use File | Settings | File Templates.
 */
public class MarginPaper extends Paper {
    double minX, minY, maxRX, maxBY;

    public void setMinImageableArea(final double x, final double y, final double w, final double h) {
        this.minX = x;
        this.minY = y;
        this.maxRX = x+w;
        this.maxBY = y+h;
        super.setImageableArea(minX, minY, maxRX, maxBY);
    }

    @Override
    public void setImageableArea(double x, double y, double w, double h) {

        if (x < minX) {
            x = minX;
        }
        if (y < minY) {
            y = minY;
        }
        if (x+w > maxRX) {
            w = maxRX - x;
        }
        if (y+h > maxBY) {
            h = maxBY - y;
        }

        super.setImageableArea(x, y, w, h);
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxRX() {
        return maxRX;
    }

    public double getMaxBY() {
        return maxBY;
    }
}
