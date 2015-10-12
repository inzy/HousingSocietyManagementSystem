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
 * LayoutEditor.java
 * ---------------
 */

package org.jpedal.examples.viewer.javabean;

import java.beans.PropertyEditorSupport;


public class LayoutEditor extends PropertyEditorSupport {

//	public LayoutEditor() {
//		setSource(this);
//	}
//
//	public LayoutEditor(Object source) {
//		if (source == null) {
//			throw new NullPointerException();
//		}
//		setSource(source);
//	}
	
	@Override
    public String[] getTags() {
		return new String[] { "Single", "Continuous", "Continuous-Facing", "Facing" };
	}

	@Override
    public void setAsText(final String s) {
		setValue(String.valueOf(s));
	}
	
//	public void setAsText(String s) {
//		if (s.equals("0"))
//			setValue(new Integer(0));
//		else if (s.equals("90"))
//			setValue(new Integer(90));
//		else if (s.equals("180"))
//			setValue(new Integer(180));
//		else if (s.equals("270"))
//			setValue(new Integer(270));
//		else
//			throw new IllegalArgumentException(s);
//	}
	
//	public String getJavaInitializationString() {
//		switch (((Number) getValue()).intValue()) {
//		default:
//		case 0:
//			return "0";
//		case 90:
//			return "90";
//		case 180:
//			return "180";
//		case 270:
//			return "270";
//		}
//	}
}
