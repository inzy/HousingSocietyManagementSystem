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
 * ViewerChooser.java
 * ---------------
 */
package org.jpedal.examples.viewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.BrowserLauncher;

/**
 * Class that loads JPedal Launcher allowing choice between Swing/JavaFX PDF
 * Viewer.
 */
public class ViewerChooser extends JFrame {

    /**
     * main method to run the software as standalone application
     */
    public static void main(final String[] args) {
        //Allows user to select either Swing or JavaFX PDF Viewer.
        final ViewerChooser vc = new ViewerChooser();
        vc.showSplash(args);

    }

    private void showSplash(final String[] args) {

        /**
         * close on exit
         */
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /**
         * Setup the Splash display.
         */
        final Color backgroundCol = new Color(0, 102, 204);
        final JPanel content = (JPanel) getContentPane();
        content.setBackground(backgroundCol); //30 80 178
        content.setLayout(new GridLayout(3, 1));
        content.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        /**
         * Set the window's bounds, centering the window.
         */
        final int width = 450;
        final int height = 250;
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = (screen.width - width) / 2;
        final int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        /**
         * Setup the Splash contents.
         */
        final Container row2 = new Container();
        row2.setLayout(new GridLayout(1, 2));
        final JButton viewerFX = new JButton("Start JavaFX PDF Viewer");
        final JButton viewerSwing = new JButton("Start Swing PDF Viewer");
        final Font btnFont = new Font("Serif", Font.BOLD, 16);
        viewerFX.setForeground(Color.WHITE);
        viewerFX.setBackground(backgroundCol);
        viewerFX.setFont(btnFont);
        viewerFX.setOpaque(true);
        viewerFX.setFont(btnFont);

        if (DecoderOptions.isRunningOnMac) {
            viewerFX.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.LIGHT_GRAY));

            viewerFX.setToolTipText("Start JavaFX PDF Viewer");
        }

        viewerSwing.setBackground(backgroundCol);
        viewerSwing.setForeground(Color.WHITE);
        viewerSwing.setFont(btnFont);
        viewerSwing.setOpaque(true);

        if (DecoderOptions.isRunningOnMac) {
            viewerSwing.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.LIGHT_GRAY));
            viewerSwing.setToolTipText("Start Swing PDF Viewer");
        }

        row2.add(viewerFX);
        row2.add(viewerSwing);

        /**
         * Add Image.
         */
        try {
            final URL path = getClass().getResource("/org/jpedal/examples/viewer/res/ViewerSelectorSplash.png");
            final ImageIcon logo = new ImageIcon(path);

            final JLabel idr = new JLabel(logo);
            idr.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(idr);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        content.add(row2);

        /**
         * Add hyperlink to Support.
         */
        final JButton url = new JButton("Visit Our Support Page");
        url.setForeground(Color.WHITE);
        url.setBackground(backgroundCol);
        url.setFont(btnFont);
        url.setAlignmentX(Component.CENTER_ALIGNMENT);
        url.setOpaque(true);

        if (DecoderOptions.isRunningOnMac) {
            url.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.LIGHT_GRAY));
            url.setToolTipText("Visit Our Support Page");
        }

        content.add(url);

        /**
         * Ensure they're running Java 8 for JavaFX Viewer Option.
         */
        final float javaVersion = Float.parseFloat(System.getProperty("java.specification.version"));
        if (javaVersion < 1.8f) {
            viewerFX.setText("ViewerFX Needs JDK 1.8+");
            viewerFX.setEnabled(false);
            if (DecoderOptions.isRunningOnMac) {
                viewerFX.setToolTipText("Visit Our Support Page");
            }
        }

        /**
         * Add Button Listeners.
         */
        url.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    BrowserLauncher.openURL("http://www.idrsolutions.com/java-pdf-library-support/");
                } catch (final Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        viewerFX.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                //Launch JavaFX PDF Viewer.
                disposeWindow();
                FXStartup.main(args);
            }
        });
        viewerSwing.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                disposeWindow();
                //Launch Swing PDF Viewer.
                final Viewer current = new Viewer();
                current.setupViewer();
                current.handleArguments(args);
            }
        });

        setVisible(true);

    }

    private void disposeWindow() {
        if (SwingUtilities.isEventDispatchThread()) {
            this.setVisible(false);
            this.dispose();
        } else {
            final Runnable doPaintComponent = new Runnable() {
                @Override
                public void run() {
                    setVisible(false);
                    dispose();
                }
            };
            SwingUtilities.invokeLater(doPaintComponent);
        }
    }

}
