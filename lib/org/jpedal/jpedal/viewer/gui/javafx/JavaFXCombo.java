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
 * JavaFXCombo.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import org.jpedal.examples.viewer.gui.generic.GUICombo;

/**
 *
 */
public class JavaFXCombo extends ComboBox implements GUICombo{

    private int ID;
    
	
	public JavaFXCombo(final String[] qualityValues) {
        final ObservableList<String> options =  FXCollections.observableArrayList(qualityValues);
        getItems().addAll(options);
	}
    
    @Override
    public void setName(final String name){
        this.setId(name);
    }
    
    
	/**
	 * @return the iD
	 */
    @Override
	public int getID() {
		return ID;
	}
	
	/**
	 * @param id the iD to set
	 */
	@Override
    public void setID(final int id) {
		ID = id;
	}
    
    
    @Override
    public void setSelectedIndex(final int defaultSelection) {
        getSelectionModel().select(defaultSelection);
    }

    @Override
    public void setToolTipText(final String tooltip){
        final Tooltip tTip = new Tooltip(tooltip);
        setTooltip(tTip);
    }
    
    @Override
    public void setEnabled(final boolean value) {
        /**
         * We reverse the value for JavaFX because Swing uses setEnable()
         * whilst javaFX uses setDisable() so false for swing is true for fx.
         */
        setDisable(!value);
    }

    @Override
    public void setSelectedItem(final Object index) {
        getSelectionModel().select(index);
    }

    @Override
    public int getSelectedIndex() {
        return getSelectionModel().getSelectedIndex();
    }

    @Override
    public Object getSelectedItem() {
        return getSelectionModel().getSelectedItem();
    }

    @Override
    public void setVisibility(final boolean set) {
        
        setVisible(set);
    }
    
}
