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
 * AppletViewer.java
 * ---------------
 */
package org.jpedal.examples.viewer;

/**standard Java stuff*/
import java.awt.Container;
import java.lang.reflect.Field;
import java.util.LinkedList;
import javax.swing.*;

import org.jpedal.io.ObjectStore;

/**
 * <br>Description: Demo to show JPedal being used
 * as a GUI viewer in an applet,
 * and to demonstrate some of JPedal's capabilities
 *
 *   See also http://files.idrsolutions.com/samplecode/org/jpedal/examples/viewer/Viewer.java.html
 */
public class AppletViewer extends JApplet{
    
    private static final long serialVersionUID = 8823940529835337414L;
    
    private LinkedList<Runnable> runnableQueue;
    
    final Viewer current = new Viewer(Values.RUNNING_APPLET);
    
    boolean isInitialised;
    boolean destroy;
    
    /** main method to run the software */
    @Override
    public void init()
    {
        
        if(!isInitialised){
            
            isInitialised=true;
            
            final String props = getParameter("propertiesFile");
            if (props != null) {
                current.loadProperties(props);
            }else{
                //If no file set use default from jar
                current.loadProperties(Viewer.PREFERENCES_DEFAULT);
            }
            
            current.setupViewer();
            
            /**
             * pass in flag and pickup - we could extend to check and set all values
             */
            final String mem = getParameter("org.jpedal.memory");
            if (mem!= null && mem.equals("true")) {
                System.setProperty("org.jpedal.memory", "true");
            }
            
            if (current.currentGUI.getFrame() instanceof JFrame) {
                this.getContentPane().add(((JFrame) current.currentGUI.getFrame()).getContentPane());
            } else {
                this.getContentPane().add((Container) current.currentGUI.getFrame());
            }
            
        }
        
    }
    
    @Override
    public void start(){
        
        //ensure setup
        init();
        
        //Check whether calling from javascript should be enabled
        final String allowJS = getParameter("allowJSCalls");
        if (allowJS!=null && allowJS.equals("true")) {
            
            runnableQueue = new LinkedList<Runnable>();
            
            final Thread t = new Thread("JS call runner") {
                @Override
                public void run() {
                    while (!destroy) {
                        
                        try {
                            Thread.sleep(200);
                        } catch (final InterruptedException e) {e.printStackTrace();}
                        
                        if (!runnableQueue.isEmpty()) {
                            final Runnable r = runnableQueue.removeFirst();
                            if (r!=null) {
                                r.run();
                            }
                        }
                    }
                }
            };
            
            t.setDaemon(true);
            t.start();
        }
        
        final String url = getParameter("openURL");
        if (url != null) {
            current.openDefaultFile(url);
        }
    }
    
    @Override
    public void destroy(){
        destroy = true;
        
        Viewer.exitOnClose=false;
        current.executeCommand(Commands.EXIT,null);
        
        //ensure cached items removed
        ObjectStore.flushPages();
    }
    
    public void executeCommand(String commandID, final String arg){
        
        if (runnableQueue == null) {
            System.out.println("Cannot call from JavaScript without setting 'allowJSCalls' parameter to true!");
            return;
        }
        
        commandID = commandID.toLowerCase();
        
        int command=0;
        if ("info".equals(commandID)) {
            command = Commands.ABOUT;
        } else if ("bitmap".equals(commandID)) {
            command = Commands.BITMAP;
        } else if ("images".equals(commandID)) {
            command = Commands.IMAGES;
        } else if ("text".equals(commandID)) {
            command = Commands.TEXT;
        } else if ("save".equals(commandID)) {
            command = Commands.SAVE;
        } else if ("print".equals(commandID)) {
            command = Commands.PRINT;
        } else if ("exit".equals(commandID)) {
            command = Commands.EXIT;
        } else if ("autoscroll".equals(commandID)) {
            command = Commands.AUTOSCROLL;
        } else if ("docinfo".equals(commandID)) {
            command = Commands.DOCINFO;
        } else if ("openfile".equals(commandID)) {
            command = Commands.OPENFILE;
        } else if ("find".equals(commandID)) {
            command = Commands.FIND;
        } else if ("snapshot".equals(commandID)) {
            command = Commands.SNAPSHOT;
        } else if ("openurl".equals(commandID)) {
            command = Commands.OPENURL;
        } else if ("visitwebsite".equals(commandID)) {
            command = Commands.VISITWEBSITE;
        } else if ("previousdocument".equals(commandID)) {
            command = Commands.PREVIOUSDOCUMENT;
        } else if ("nextdocument".equals(commandID)) {
            command = Commands.NEXTDOCUMENT;
        } else if ("previousresult".equals(commandID)) {
            command = Commands.PREVIOUSRESULT;
        } else if ("nextresult".equals(commandID)) {
            command = Commands.NEXTRESULT;
        } else if ("tip".equals(commandID)) {
            command = Commands.TIP;
//        } else if ("update".equals(commandID)) {
//            command = Commands.UPDATE;
        } else if ("preferences".equals(commandID)) {
            command = Commands.PREFERENCES;
        } else if ("copy".equals(commandID)) {
            command = Commands.COPY;
        } else if ("selectall".equals(commandID)) {
            command = Commands.SELECTALL;
        } else if ("deselectall".equals(commandID)) {
            command = Commands.DESELECTALL;
        } else if ("updateguilayout".equals(commandID)) {
            command = Commands.UPDATEGUILAYOUT;
        } else if ("mousemode".equals(commandID)) {
            command = Commands.MOUSEMODE;
        } else if ("panmode".equals(commandID)) {
            command = Commands.PANMODE;
        } else if ("textselect".equals(commandID)) {
            command = Commands.TEXTSELECT;
        } else if ("separatecover".equals(commandID)) {
            command = Commands.SEPARATECOVER;
        } else if ("firstpage".equals(commandID)) {
            command = Commands.FIRSTPAGE;
        } else if ("fbackpage".equals(commandID)) {
            command = Commands.FBACKPAGE;
        } else if ("backpage".equals(commandID)) {
            command = Commands.BACKPAGE;
        } else if ("forwardpage".equals(commandID)) {
            command = Commands.FORWARDPAGE;
        } else if ("fforwardpage".equals(commandID)) {
            command = Commands.FFORWARDPAGE;
        } else if ("lastpage".equals(commandID)) {
            command = Commands.LASTPAGE;
        } else if ("goto".equals(commandID)) {
            command = Commands.GOTO;
        } else if ("single".equals(commandID)) {
            command = Commands.SINGLE;
        } else if ("continuous".equals(commandID)) {
            command = Commands.CONTINUOUS;
        } else if ("continuous_facing".equals(commandID)) {
            command = Commands.CONTINUOUS_FACING;
        } else if ("facing".equals(commandID)) {
            command = Commands.FACING;
        } else if ("pageflow".equals(commandID)) {
            command = Commands.PAGEFLOW;
        } else if ("fullscreen".equals(commandID)) {
            command = Commands.FULLSCREEN;
        } else if ("rss".equals(commandID)) {
            command = Commands.RSS;
        } else if ("help".equals(commandID)) {
            command = Commands.HELP;
        } else if ("buy".equals(commandID)) {
            command = Commands.BUY;
        } else if ("quality".equals(commandID)) {
            command = Commands.QUALITY;
        } else if ("rotation".equals(commandID)) {
            command = Commands.ROTATION;
        } else if ("scaling".equals(commandID)) {
            command = Commands.SCALING;
        } else if ("saveform".equals(commandID)) {
            command = Commands.SAVEFORM;
        } else if ("pdf".equals(commandID)) {
            command = Commands.PDF;
        } else if ("rotate".equals(commandID)) {
            command = Commands.ROTATE;
        } else if ("delete".equals(commandID)) {
            command = Commands.DELETE;
        } else if ("add".equals(commandID)) {
            command = Commands.ADD;
        } else if ("security".equals(commandID)) {
            command = Commands.SECURITY;
        } else if ("addheaderfooter".equals(commandID)) {
            command = Commands.ADDHEADERFOOTER;
        } else if ("stamptext".equals(commandID)) {
            command = Commands.STAMPTEXT;
        } else if ("stampimage".equals(commandID)) {
            command = Commands.STAMPIMAGE;
        } else if ("setcrop".equals(commandID)) {
            command = Commands.SETCROP;
        } else if ("nup".equals(commandID)) {
            command = Commands.NUP;
        } else if ("handouts".equals(commandID)) {
            command = Commands.HANDOUTS;
        } else if ("scroll".equals(commandID)) {
            command = Commands.SCROLL;
        } else if ("addview".equals(commandID)) {
            command = Commands.ADDVIEW;
        } else if ("forward".equals(commandID)) {
            command = Commands.FORWARD;
        } else if ("back".equals(commandID)) {
            command = Commands.BACK;
        } else {
            //Check if command exists via reflection
            try {
                final Field f = Commands.class.getField(commandID.toUpperCase());
                command = f.getInt(f);
            } catch(final Exception e) {e.printStackTrace();}
        }
        
        //Generate normal args
        final String[] args;
        if (arg != null) {
            args = new String[]{arg};
        } else {
            args = null;
        }
        
        final int finalCommand = command;
        
        //Add to queue
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                current.executeCommand(finalCommand, args);
            }
        };
        runnableQueue.addLast(r);
    }
}
