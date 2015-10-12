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
 * ViewerInt.java
 * ---------------
 */

package org.jpedal.examples.viewer;


/** PDF viewer
 *
 * If you are compiling, you will need to download all the examples source files from http://www.idrsolutions.com/how-to-view-pdf-files-in-java/
 *
 * Run directly from jar with java -cp jpedal.jar org/jpedal/examples/viewer/Viewer
 * or java -jar jpedal.jar
 *
 * Lots of tutorials on how to configure on our website
 * 
 * There is also a JavaFX Viewer documented at
 * http://files.idrsolutions.com/samplecode/org/jpedal/examples/viewer/ViewerFX.java.html
 *
 * If you want to implement your own
 * Very simple example at http://files.idrsolutions.com/samplecode/org/jpedal/examples/jpaneldemo/JPanelDemo.java.html
 * But we would recommend you look at the full viewer as it is totally configurable and does everything for you.
 *
 * See also http://javadoc.idrsolutions.com/org/jpedal/constants/JPedalSettings.html for settings to customise
 *
 * Fully featured GUI viewer and demonstration of JPedal's capabilities
 *
 * <br>This class provides the framework for the Viewer and calls other classes which provide the following
 * functions:-
 *
 * <br>Values commonValues - repository for general settings
 * Printer currentPrinter - All printing functions and access methods to see if printing active
 * PdfDecoder decode_pdf - PDF library and panel
 * ThumbnailPanel thumbnails - provides a thumbnail pane down the left side of page - thumbnails can be clicked on to goto page
 * PropertiesFile properties - saved values stored between sessions
 * SwingGUI currentGUI - all Swing GUI functions
 * SearchWindow searchFrame (not GPL) - search Window to search pages and goto references on any page
 * Commands currentCommands - parses and executes all options
 * SwingMouseHandler mouseHandler - handles all mouse and related activity
 */
public interface ViewerInt {
	


    /**
     *
     * @param defaultFile
     * Allow user to open PDF file to display
     */
    public void openDefaultFile(String defaultFile);

    /**
     *
     * @param defaultFile
     * Allow user to open PDF file to display
     */
    public void openDefaultFileAtPage(String defaultFile, int page);
    
    public void setRootContainer(Object rootContainer);

    /**
     * Should be called before setupViewer
     */
    public void loadProperties(String props);

    /**
     * initialise and run client (default as Application in own Frame)
     */
    public void setupViewer();
    
    /**
     * Have the viewer handle program arguments
     * @param args :: Program arguments passed into the Viewer.
     */
    public void handleArguments(String[] args);

    /**
     * Execute Jpedal functionality from outside of the library using this method.
     * EXAMPLES
     *    commandID = Commands.OPENFILE, args = {"/PDFData/Hand_Test/crbtrader.pdf}"
     *    commandID = Commands.OPENFILE, args = {byte[] = {0,1,1,0,1,1,1,0,0,1}, "/PDFData/Hand_Test/crbtrader.pdf}"
     *    commandID = Commands.ROTATION, args = {"90"}
     *    commandID = Commands.OPENURL,  args = {"http://www.cs.bham.ac.uk/~axj/pub/papers/handy1.pdf"}
     *
     * for full details see http://www.idrsolutions.com/access-pdf-viewer-features-from-your-code/
     *
     * @param commandID :: static int value from Commands to spedify which command is wanted
     * @param args :: arguements for the desired command
     *
     */
    @SuppressWarnings("UnusedReturnValue")
    public Object executeCommand(int commandID, Object[] args);


    /**
     * Allows external helper classes to be added to JPedal to alter default functionality.
     * <br><br>If Options.FormsActionHandler is the type then the <b>newHandler</b> should be
     * of the form <b>org.jpedal.objects.acroforms.ActionHandler</b>
     * <br><br>If Options.JPedalActionHandler is the type then the <b>newHandler</b> should be
     * of the form <b>Map</b> which contains Command Integers, mapped onto their respective
     * <b>org.jpedal.examples.viewer.gui.swing.JPedalActionHandler</b> implementations.  For example,
     * to create a custom help action, you would add to your map, Integer(Commands.HELP) ->  JPedalActionHandler.
     * For a tutorial on creating custom actions in the Viewer, see
     * <b>http://www.jpedal.org/support.php</b>
     *
     * @param newHandler
     * @param type
     */
    public void addExternalHandler(Object newHandler, int type);

    /**
     * run with caution and only at end of usage if you really need
     */
    public void dispose();
    
}
