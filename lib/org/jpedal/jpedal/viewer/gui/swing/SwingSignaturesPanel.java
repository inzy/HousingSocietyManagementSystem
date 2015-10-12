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
 * SwingSignaturesPanel.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.util.Iterator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.gui.SignaturesTreeCellRenderer;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;


/**
 *
 * @author Simon
 */
public class SwingSignaturesPanel extends JTree {
    
    public SwingSignaturesPanel(){
        final SignaturesTreeCellRenderer treeCellRenderer = new SignaturesTreeCellRenderer();
        setCellRenderer(treeCellRenderer);
    }
    
    public void reinitialise(final PdfDecoderInt decode_pdf, final Iterator<FormObject> signatureObjects){
        
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Signatures");

        final DefaultMutableTreeNode signed = new DefaultMutableTreeNode("The following have digitally counter-signed this document");

        final DefaultMutableTreeNode blank = new DefaultMutableTreeNode("The following signature fields are not signed");

        while(signatureObjects.hasNext()){

            final FormObject formObj = signatureObjects.next();

            final PdfObject sigObject=formObj.getDictionary(PdfDictionary.V);

            decode_pdf.getIO().checkResolved(formObj);

            if(sigObject == null){

                if(!blank.isNodeChild(root)) {
                    root.add(blank);
                }

                final DefaultMutableTreeNode blankNode = new DefaultMutableTreeNode(formObj.getTextStreamValue(PdfDictionary.T)+ " on page " + formObj.getPageNumber());
                blank.add(blankNode);

            } else {

                if(!signed.isNodeChild(root)) {
                    root.add(signed);
                }

                //String name = (String) OLDsigObject.get("Name");

                final String name=sigObject.getTextStreamValue(PdfDictionary.Name);

                final DefaultMutableTreeNode owner = new DefaultMutableTreeNode("Signed by " + name);
                signed.add(owner);

                final DefaultMutableTreeNode type = new DefaultMutableTreeNode("Type");
                owner.add(type);

                String filter = null;//sigObject.getName(PdfDictionary.Filter);

                //@simon -new version to test
                final PdfArrayIterator filters = sigObject.getMixedArray(PdfDictionary.Filter);
                if(filters!=null && filters.hasMoreTokens()) {
                    filter = filters.getNextValueAsString(true);
                }

                final DefaultMutableTreeNode filterNode = new DefaultMutableTreeNode("Filter: " + filter);
                type.add(filterNode);

                final String subFilter = sigObject.getName(PdfDictionary.SubFilter);

                final DefaultMutableTreeNode subFilterNode = new DefaultMutableTreeNode("Sub Filter: " + subFilter);
                type.add(subFilterNode);

                final DefaultMutableTreeNode details = new DefaultMutableTreeNode("Details");
                owner.add(details);


                //@simon - guess on my part....
                final String rawDate=sigObject.getTextStreamValue(PdfDictionary.M);
                if(rawDate!=null){

                    final StringBuilder date = new StringBuilder(rawDate);

                    date.delete(0, 2);
                    date.insert(4, '/');
                    date.insert(7, '/');
                    date.insert(10, ' ');
                    date.insert(13, ':');
                    date.insert(16, ':');
                    date.insert(19, ' ');

                    final DefaultMutableTreeNode time = new DefaultMutableTreeNode("Time: " +date);
                    details.add(time);
                }else{
                    final DefaultMutableTreeNode time = new DefaultMutableTreeNode("Time: unset");
                    details.add(time); 
                }

                final String reason=sigObject.getTextStreamValue(PdfDictionary.Reason);

                final DefaultMutableTreeNode reasonNode = new DefaultMutableTreeNode("Reason: " + reason);
                details.add(reasonNode);

                final String location=sigObject.getTextStreamValue(PdfDictionary.Location);

                final DefaultMutableTreeNode locationNode = new DefaultMutableTreeNode("Location: " + location);
                details.add(locationNode);

                final DefaultMutableTreeNode field = new DefaultMutableTreeNode("Field: " + formObj.getTextStreamValue(PdfDictionary.T)+ " on page " + formObj.getPageNumber());
                details.add(field);
            }
        }
        ((DefaultTreeModel)getModel()).setRoot(root);

    }
}
