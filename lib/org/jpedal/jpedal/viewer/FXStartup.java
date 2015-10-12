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
 * FXStartup.java
 * ---------------
 */

package org.jpedal.examples.viewer;

import java.util.List;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import static org.jpedal.examples.viewer.ViewerFX.checkUserJavaVersion;
import org.jpedal.parser.DecoderOptions;

/**
 * startup our Viewer as Application and allow user to access Viewer
 */
public class FXStartup extends Application {
    private ProgressBar loadProgress;
    private Label progressText;
    ViewerFX viewer;
    private Pane splashLayout;
    private final Stage splashStage = new Stage();
    private static final int SPLASH_WIDTH = 600;
    private static final int SPLASH_HEIGHT = 200;
    
    public static void main(final String[] args) {
        DecoderOptions.javaVersion = Float.parseFloat(System.getProperty("java.specification.version"));
        checkUserJavaVersion();
        launch(args);
    }
    @Override
    //Initalising the stage for the SplashScreen
     public void init()
    {
        final String imgPath;
        final String barColour;
        //<start-fx>
        //setup viewer image
        imgPath = "/org/jpedal/examples/viewer/res/viewerFXSplash.png";
        barColour =("-fx-accent: blue;");
        /*
        //<end-fx>
        //setup reader image
        imgPath="/org/jpedal/examples/viewer/res/OSFXSplash.png"; 
        barColour =("-fx-accent: purple;");
        /*
         */
        final ImageView splash = new ImageView(getClass().getResource(imgPath).toExternalForm());
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH);
        progressText = new Label("All modules are loaded.");
        loadProgress.setStyle(barColour);
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setEffect(new DropShadow());  
    }

    @Override
    
    public void start(final Stage initstage){
        //Debug mode
        final boolean debugSplash  = false;
        if(debugSplash){
            System.out.println("Starting the SplashScreen");
            final Task<ObservableList<String>> loadModsTask = new Task() {
                @Override
                protected ObservableList<String> call() throws InterruptedException {
                    final ObservableList<String> loadMods
                        = FXCollections.observableArrayList();
                    final ObservableList<String> availableFriends
                        = FXCollections.observableArrayList("Network Module", "User Module", "User Interface", "User Controls");

                    updateMessage("Loading. . .");
                    for (int i = 0; i < availableFriends.size(); i++) {
                        Thread.sleep(900);
                        updateProgress(i + 1, availableFriends.size());
                        final String nextFriend = availableFriends.get(i);
                        loadMods.add(nextFriend);
                        updateMessage("Loading . . .  " + nextFriend);
                    }
                    Thread.sleep(500);
                    updateMessage("All Modules are loaded.");

                    return loadMods;
                }

            };
            showSplash(loadModsTask);

            loadModsTask.setOnSucceeded(new EventHandler() {
                @Override
                public void handle(final Event event) {
                    startNew(initstage);
                }
            });

            new Thread(loadModsTask).start();
        }
        //if debug not true then start viewer
        else{
            startNew(initstage);
        }
    }
    
    //starting the FXViewer Stage
     public void startNew(final Stage stage){
        
        final List<String> args = this.getParameters().getUnnamed();
        viewer = new ViewerFX(stage,args.toArray(new String[args.size()]));
        viewer.setupViewer();
    }
    //Starting the splash screen
    private void showSplash(final Task task){
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener(new ChangeListener<Worker.State>()
        {
            @Override
            public void changed(final ObservableValue<? extends Worker.State> observableValue, final Worker.State oldState, final Worker.State newState)
            {
                if (newState == Worker.State.SUCCEEDED)
                {
                    loadProgress.progressProperty().unbind();
                    loadProgress.setProgress(1);
                    splashStage.toFront();
                    final FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                    fadeSplash.setFromValue(1.0);
                    fadeSplash.setToValue(0.0);
                     fadeSplash.setOnFinished(new EventHandler<ActionEvent>()
                    {
                        @Override
                        public void handle(final ActionEvent actionEvent)
                        {
                                splashStage.hide();
                        }
                    });
                    fadeSplash.play();
                    
                }
            }
            
        });
        final Scene splashScene = new Scene(splashLayout);
        splashStage.initStyle(StageStyle.UNDECORATED);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        splashStage.setScene(splashScene);
        splashStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        splashStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        splashStage.show(); 
    }
            
    public ViewerFX getViewer(){
        return viewer;
    }
}