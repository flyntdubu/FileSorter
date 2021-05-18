package com.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This will act as a controller application that manages loading game windows as well as
 * loading FXML files.
 */
public class Controller extends Application {

    /**
     * The game stage.
     */
    private static Stage mainStage;

    public static void main(String[] args) {
        launch( args );
    }

    /**
     * This will initialize the window to the title screen.
     *
     * @param primaryStage The initial stage
     */
    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        mainStage.setTitle("Art Sorter");
        changeScreen( new MainScreen() );
    }

    /**
     * This will change the screen to the parent screen
     * passed into this method. Pass your panes here :)
     *
     * @param screen the JavaFX parent to change to
     */
    public static void changeScreen(Parent screen) {
        mainStage.setScene( new Scene( screen ) );
        mainStage.show();
    }

    /**
     * This will load your FXML file.
     *
     * @param fxmlPath   The FXML filename
     * @param controller the controller class
     */
    public static void loadFxMLFile(String fxmlPath, Object controller) {
        FXMLLoader fxLoader = new FXMLLoader( ClassLoader.getSystemResource(fxmlPath));
        fxLoader.setRoot(controller);
        fxLoader.setController(controller);

        try {
            fxLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
    * Returns the main stage
     */
    public static Stage getMainStage() {
        return mainStage;
    }

}