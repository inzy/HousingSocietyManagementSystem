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
 * Info.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.jpedal.PdfDecoderInt;
import org.jpedal.display.GUIDisplay;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

/**
 * Code to handle Info button we display in demo versions of Viewer
 */
public class Info {

    private static final Font textFont1 = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font headFont = new Font("SansSerif", Font.BOLD, 14);

    public static void execute(final Object[] args, final GUIFactory currentGUI) {
        if (args == null) {
            getInfoBox(currentGUI);
        } else {

        }
    }

    /**
     * display a box giving user info about program
     */
    private static void getInfoBox(final GUIFactory currentGUI) {

        final JPanel details = new JPanel();
        details.setPreferredSize(new Dimension(400, 260));
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        //general details
        final JLabel header1 = new JLabel(Messages.getMessage("PdfViewerInfo.title"));
        header1.setOpaque(false);
        header1.setFont(headFont);
        header1.setAlignmentX(Component.CENTER_ALIGNMENT);
        details.add(header1);

        details.add(Box.createRigidArea(new Dimension(0, 15)));

        final String xmlText = Messages.getMessage("PdfViewerInfo1");
        if (!xmlText.isEmpty()) {

            final JTextArea xml = new JTextArea();
            xml.setFont(textFont1);
            xml.setOpaque(false);
            xml.setText(xmlText + "\n\nVersions\n JPedal: " + PdfDecoderInt.version + "          " + "Java: " + System.getProperty("java.version"));
            xml.setLineWrap(true);
            xml.setWrapStyleWord(true);
            xml.setEditable(false);
            details.add(xml);
            xml.setAlignmentX(Component.CENTER_ALIGNMENT);

        }

        final ImageIcon logo = new ImageIcon(Info.class.getClass().getResource("/org/jpedal/examples/viewer/res/logo.gif"));
        details.add(Box.createRigidArea(new Dimension(0, 25)));
        final JLabel idr = new JLabel(logo);
        idr.setAlignmentX(Component.CENTER_ALIGNMENT);
        details.add(idr);

        final JLabel url = new JLabel("<html><center>" + Messages.getMessage("PdfViewerJpedalLibrary.Text")
                + Messages.getMessage("PdfViewer.WebAddress"));
        url.setForeground(Color.blue);
        url.setHorizontalAlignment(JLabel.CENTER);
        url.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Create cursor control
        url.addMouseListener(new MouseListener() {

            @Override
            public void mouseEntered(final MouseEvent e) {
                if (GUIDisplay.allowChangeCursor) {
                    details.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                url.setText("<html><center>" + Messages.getMessage("PdfViewerJpedalLibrary.Link")
                        + Messages.getMessage("PdfViewerJpedalLibrary.Text")
                        + Messages.getMessage("PdfViewer.WebAddress") + "</a></center>");
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                if (GUIDisplay.allowChangeCursor) {
                    details.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                url.setText("<html><center>" + Messages.getMessage("PdfViewerJpedalLibrary.Text")
                        + Messages.getMessage("PdfViewer.WebAddress"));
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                try {
                    BrowserLauncher.openURL(Messages.getMessage("PdfViewer.VisitWebsite"));
                } catch (final Exception e1) {
                    //currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));

                    
                    if(LogWriter.isOutput()) { 
                         LogWriter.writeLog("Exception attempting launch browser: " + e1); 
                     } 
                }
            }

            @Override
            public void mousePressed(final MouseEvent e) {
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
            }
        });

        details.add(url);
        details.add(Box.createRigidArea(new Dimension(0, 5)));

        details.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        currentGUI.showMessageDialog(details, Messages.getMessage("PdfViewerInfo3"), JOptionPane.PLAIN_MESSAGE);

    }
}
