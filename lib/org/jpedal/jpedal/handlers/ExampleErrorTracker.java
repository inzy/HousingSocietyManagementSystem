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
 * ExampleErrorTracker.java
 * ---------------
 */
package org.jpedal.examples.handlers;

import org.jpedal.external.ErrorTracker;
import org.jpedal.io.DefaultErrorTracker;

public class ExampleErrorTracker extends DefaultErrorTracker implements ErrorTracker {

    long timeAtStart;

    boolean userFailedPage;

    public ExampleErrorTracker() {}


    @Override
    public String getPageFailureMessage() {

        if(userFailedPage) {
            return "Timed out on page";
        } else {
            return super.getPageFailureMessage();
        }
    }

    @Override
    //use to see if page failed
    public boolean ispageSuccessful() {
        if(userFailedPage) {
            return false;
        } else {
            return super.ispageSuccessful();
        }
    }

    @Override
    // called every time we execute a Postscript command in data streams
    // (dataPointer/streamSize) gives indicator of amount decoded but page can
    // contain multiple streams
    public boolean checkForExitRequest(final int dataPointer, final int streamSize) {

        //gracefully allow us to fail if over 15 seconds to decode
        final long secondsElapsed=(System.currentTimeMillis()-timeAtStart)/1000;
        System.out.println("Seconds elapsed="+secondsElapsed);
        if(secondsElapsed>15){
            userFailedPage=true;
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void finishedPageDecoding(final int rawPage) {
        System.out.println("----Completed page "+rawPage);
    }

    @Override
    //called when we start decoding page
    public void startedPageDecoding(final int rawPage) {
        System.out.println("----Started page "+rawPage);

        // get the current time
        timeAtStart=System.currentTimeMillis();
    }
}
