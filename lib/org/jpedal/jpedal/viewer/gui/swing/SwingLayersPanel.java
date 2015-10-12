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
 * SwingLayersPanel.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.jpedal.PdfDecoder;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.gui.CheckNode;
import org.jpedal.examples.viewer.gui.CheckRenderer;
import org.jpedal.examples.viewer.gui.generic.GUILayersPanel;
import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.objects.layers.PdfLayerList;

/**
 *
 * @author Simon
 */
public class SwingLayersPanel extends JPanel implements GUILayersPanel {
    private PdfLayerList layersObject;
    private final DefaultMutableTreeNode topLayer;
    
    public SwingLayersPanel(){
        topLayer = new DefaultMutableTreeNode("Layers");
    }
    
    @Override
    public void reinitialise(final PdfLayerList layersObject, final PdfDecoderInt decode_pdf, 
                             final Object scrollPane, final int currentPage){
        this.layersObject = layersObject;
        
        removeAll();
        setLayout(new BorderLayout());
        
        
        /**
        * add metadata to tab (Map of key values) as a Tree
        */
        final DefaultMutableTreeNode top = new DefaultMutableTreeNode("Info");

        final Map metaData=layersObject.getMetaData();

        final Iterator metaDataKeys=metaData.keySet().iterator();
        Object nextKey, value;
        while(metaDataKeys.hasNext()){

            nextKey=metaDataKeys.next();
            value=metaData.get(nextKey);

            top.add(new DefaultMutableTreeNode(nextKey+"="+value));

        }

        //add collapsed Tree at Top
        final JTree infoTree = new JTree(top);
        infoTree.setToolTipText("Double click to see any metadata");
        infoTree.setRootVisible(true);
        infoTree.collapseRow(0);
        add(infoTree, BorderLayout.NORTH);
        
        /**
         * Display list of layers which can be recursive
         * layerNames can contain comments or sub-trees as Object[] or String name of Layer
         */
        final Object[] layerNames=layersObject.getDisplayTree();
        if(layerNames!=null){

            topLayer.removeAllChildren();

            final JTree layersTree = new JTree(topLayer);
            layersTree.setName("LayersTree");

            //Listener to redraw with altered layer
            layersTree.addTreeSelectionListener(new TreeSelectionListener() {

                @Override
                public void valueChanged(final TreeSelectionEvent e) {

                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    layersTree.getLastSelectedPathComponent();

                    /* exit if nothing is selected */
                    if (node == null) {
                        return;
                    }

                    /* retrieve the full name of Layer that was selected */
                    StringBuilder rawName = new StringBuilder((String)node.getUserObject());

                    //and add path
                    final Object[] patentNames = ((DefaultMutableTreeNode)node.getParent()).getUserObjectPath();
                    final int size= patentNames.length;
                    for(int jj=size-1;jj>0;jj--){ //note I ingore 0 which is root and work backwards
                            rawName.append(PdfLayerList.deliminator).append(patentNames[jj]);
                    }

                    final String name=rawName.toString();

                    //if allowed toggle and update display
                    if(layersObject.isLayerName(name) && !layersObject.isLocked(name)){

                        //toggle layer status when clicked
                        final Runnable updateAComponent = new Runnable() {

                            @Override
                            public void run() {
                                ((PdfDecoder)decode_pdf).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                //force refresh
                                ((PdfDecoder)decode_pdf).invalidate();
                                ((PdfDecoder)decode_pdf).updateUI();
                                ((PdfDecoder)decode_pdf).validate();

                                // [AWI]: Refresh the scrollpane if it is available
                                if (scrollPane != null) {
                                    ((JScrollPane)scrollPane).invalidate();
                                    ((JScrollPane)scrollPane).updateUI();
                                    ((JScrollPane)scrollPane).validate();
                                }

                                //update settings on display and in PdfDecoder
                                final CheckNode checkNode=(CheckNode)node;

                                if(!checkNode.isEnabled()){ //selection not allowed so display info message

                                    checkNode.setSelected(checkNode.isSelected());
                                    ShowGUIMessage.showstaticGUIMessage(new StringBuffer("This layer has been disabled because its parent layer is disabled"),"Parent Layer disabled");
                                }else{
                                    final boolean reversedStatus=!checkNode.isSelected();
                                    checkNode.setSelected(reversedStatus);
                                    layersObject.setVisiblity(name,reversedStatus);

                                    //may be radio buttons which disable others so sync values
                                    //before repaint
                                    syncTreeDisplay(topLayer,true);

                                    //decode again with new settings
                                    try {
                                        decode_pdf.decodePage(currentPage);
                                    } catch (final Exception e) {
                                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                    }

                                }
                                //deselect so works if user clicks on same again to deselect
                                layersTree.invalidate();
                                layersTree.clearSelection();
                                layersTree.repaint();
                                ((PdfDecoder)decode_pdf).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            }
                        };

                        SwingUtilities.invokeLater(updateAComponent);

                    }
                }
            });

            //build tree from values
            topLayer.removeAllChildren();
            addLayersToTree(layerNames, topLayer, true, layersObject);

            layersTree.setRootVisible(true);
            layersTree.expandRow(0);
            layersTree.setCellRenderer(new CheckRenderer());
            layersTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

            add(layersTree, BorderLayout.CENTER);

        }
    }
    
    private static void addLayersToTree(final Object[] layerNames, DefaultMutableTreeNode topLayer, boolean isEnabled, final PdfLayerList layersObject) {

		String name;

		DefaultMutableTreeNode currentNode=topLayer;
		boolean parentEnabled=isEnabled,parentIsSelected=true;

        for (final Object layerName : layerNames) {

            //work out type of node and handle
            if (layerName instanceof Object[]) { //its a subtree

                final DefaultMutableTreeNode oldNode = currentNode;

                addLayersToTree((Object[]) layerName, currentNode, isEnabled && parentIsSelected, layersObject);

                currentNode = oldNode;
                //if(currentNode!=null)
                //currentNode= (DefaultMutableTreeNode) currentNode.getParent();

                isEnabled = parentEnabled;
            } else {

                //store possible recursive settings
                parentEnabled = isEnabled;

                if (layerName == null) {
                    continue;
                }

                if (layerName instanceof String) {
                    name = (String) layerName;
                } else //its a byte[]
                {
                    name = new String((byte[]) layerName);
                }

                /**
                 * remove full path in name
                 */
                String title = name;
                final int ptr = name.indexOf(PdfLayerList.deliminator);
                if (ptr != -1) {
                    title = title.substring(0, ptr);

                }

                if (name.endsWith(" R")) { //ignore
                } else if (!layersObject.isLayerName(name)) { //just text

                    currentNode = new DefaultMutableTreeNode(title);
                    topLayer.add(currentNode);
                    topLayer = currentNode;

                    parentIsSelected = true;

                    //add a node
                } else if (topLayer != null) {

                    currentNode = new CheckNode(title);
                    topLayer.add(currentNode);

                    //see if showing and set box to match
                    if (layersObject.isVisible(name)) {
                        ((CheckNode) currentNode).setSelected(true);
                        parentIsSelected = true;
                    } else {
                        parentIsSelected = false;
                    }

                    //check locks and allow Parents to disable children
                    if (isEnabled) {
                        isEnabled = !layersObject.isLocked(name);
                    }

                    ((CheckNode) currentNode).setEnabled(isEnabled);
                }
            }
        }
	}
    
    @Override
    public void resetLayers(){
        topLayer.removeAllChildren();
    }
    
    @Override
    public void rescanPdfLayers(){
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                //refresh the layers tab so JS updates are carried across.
                syncTreeDisplay(topLayer,true);
                invalidate();
                repaint();
            } else {
                final Runnable doPaintComponent = new Runnable() {
                    @Override
                    public void run() {
                        //refresh the layers tab so JS updates are carried across.
                        syncTreeDisplay(topLayer,true);
                        invalidate();
                        repaint();
                    }
                };
                SwingUtilities.invokeAndWait(doPaintComponent);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void syncTreeDisplay(final DefaultMutableTreeNode topLayer, boolean isEnabled) {

        final int count=topLayer.getChildCount();

        final boolean parentIsEnabled =isEnabled;
        boolean isSelected;
        String childName;
        TreeNode childNode;
        int ii=0;
        while(true){

            isEnabled= parentIsEnabled;
            isSelected=true;

            if(count==0) {
                childNode = topLayer;
            } else {
                childNode = topLayer.getChildAt(ii);
            }

            if(childNode instanceof CheckNode){

                final CheckNode cc=(CheckNode)childNode;
                childName=(String)cc.getText();

                if(layersObject.isLayerName(childName)){

                    if(isEnabled) {
                        isEnabled = !layersObject.isLocked(childName);
                    }

                    isSelected=(layersObject.isVisible(childName));
                    cc.setSelected(isSelected);
                    cc.setEnabled(isEnabled);
                }
            }

            if(childNode.getChildCount()>0){

                final Enumeration children=childNode.children();
                while(children.hasMoreElements()) {
                    syncTreeDisplay((DefaultMutableTreeNode) children.nextElement(), (isEnabled && isSelected));
                }
            }

            ii++;
            if(ii>=count) {
                break;
            }
        }
    }
}
