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
 * SampleHTMLImageController.java
 * ---------------
 */
package org.jpedal.examples.handlers;

import org.jpedal.objects.GraphicsState;
import org.jpedal.render.output.OutputImageController;

import java.awt.image.BufferedImage;

public class SampleHTMLImageController implements OutputImageController {
    @Override
    public BufferedImage processImage(final BufferedImage image, final GraphicsState gs, final String name) {
        return image;
    }
}