package com.main;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;


import javafx.event.ActionEvent;
import java.io.File;
import java.util.Optional;


public class MainScreen extends GridPane {

    private DirectoryChooser srcChooser;
    private DirectoryChooser dstChooser;

    private File srcDirectory;
    private File dstDirectory;

    private static Sorter sorter;


    @FXML
    private TextField srcField;

    @FXML
    private TextField dstField;

    @FXML
    private Button sortButton;

    @FXML
    private Button modeButton;

    public MainScreen() {

        /** Load FXML File */
        Controller.loadFxMLFile("MainScreen.fxml", this);

        /** Create chooser for Source */
        this.srcChooser = new DirectoryChooser();
        srcChooser.setInitialDirectory(new File("src"));

        /** Create chooser for Destination */
        this.dstChooser = new DirectoryChooser();
        dstChooser.setInitialDirectory(new File("src"));

        this.sorter = new Sorter();

    }

    /**
     * Brings up source selection
     * @param e
     */
    @FXML
    public void selectSource(ActionEvent e) {

        File oldSrc = srcDirectory;

        srcDirectory = srcChooser.showDialog(Controller.getMainStage());

        if (srcDirectory != null) {
            srcField.setText(srcDirectory.getAbsolutePath());
        } else {
            srcDirectory = oldSrc;
        }

    }

    /**
     * Brings up destination selection
     * @param e
     */
    @FXML
    public void selectDst(ActionEvent e) {

        File oldDst = dstDirectory;

        dstDirectory = dstChooser.showDialog(Controller.getMainStage());

        if (dstDirectory != null) {
            dstField.setText(dstDirectory.getAbsolutePath());
        } else {
            dstDirectory = oldDst;
        }

        checkReady();

    }

    /**
     * Calls sort and outputs result as an alert
     * @param e dunno lmao
     */
    @FXML
    public void beginSort(ActionEvent e) {

        Alert resultAlert = new Alert(Alert.AlertType.NONE);

        String result = sorter.sort(srcDirectory, dstDirectory);

        if (result.contains("ERR")) {
            resultAlert.setAlertType(Alert.AlertType.ERROR);
            resultAlert.setHeaderText("Error Detected!");
        } else {
            resultAlert.setAlertType(Alert.AlertType.INFORMATION);
            resultAlert.setHeaderText("Sorted Successfully!");
        }

        result = result.replaceAll("ERR", " ");
        resultAlert.setContentText(result);
        resultAlert.showAndWait();


//        TODO- IMPLEMENT THIS HERE ITS COOL I THINK PROBABLY
//        if (result.contains("duplicate(s)")) {
//
//            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
//            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
//
//            resultAlert = new Alert(Alert.AlertType.CONFIRMATION,
//                        "Would you like to see the duplicate(s)?",
//                            yes,
//                            no);
//
//            resultAlert.setTitle("Duplicates Detected!");
//
//            Optional<ButtonType> showDupes = resultAlert.showAndWait();
//
//            if (showDupes.orElse(no) == yes) {
//
//            }
//        }

    }

    /** Enables button if src and dst are set */
    private void checkReady() {
        if (srcDirectory != null && dstDirectory != null) {
            sortButton.setDisable(false);
        }
    }

    /**
     * Toggles Movement Mode
     * @param e duno lmao
     */
    @FXML
    public void switchMode(ActionEvent e) {

        if (modeButton.getText().equals("Copy Mode")) {
            modeButton.setText("Move Mode");
            sorter.setMode(1);
        } else {
            modeButton.setText("Copy Mode");
            sorter.setMode(0);
        }

    }

}