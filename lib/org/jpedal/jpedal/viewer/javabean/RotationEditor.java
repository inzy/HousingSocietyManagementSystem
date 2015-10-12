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
 * RotationEditor.java
 * ---------------
 */

package org.jpedal.examples.viewer.javabean;

import java.beans.PropertyEditorSupport;

public class RotationEditor extends PropertyEditorSupport {

//	public RotationEditor() {
//		setSource(this);
//	}
//
//	public RotationEditor(Object source) {
//		if (source == null) {
//			throw new NullPointerException();
//		}
//		setSource(source);
//	}
	
	@Override
    public String[] getTags() {
		return new String[] { "0", "90", "180", "270" };
	}

	@Override
    public void setAsText(final String s) {
        
        int rot=Integer.parseInt(s);
		
        if (rot==0) {
            setValue(0);
        } else if (rot==90) {
            setValue(90);
        } else if (rot==180) {
            setValue(180);
        } else if (rot==270) {
            setValue(270);
        } else {
            throw new IllegalArgumentException(s);
        }
	}
	
	@Override
    public String getJavaInitializationString() {
		switch (((Number) getValue()).intValue()) {
		default:
		case 0:
			return "0";
		case 90:
			return "90";
		case 180:
			return "180";
		case 270:
			return "270";
		}
	}
}
