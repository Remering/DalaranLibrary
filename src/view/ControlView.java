package view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import controller.ControlController;


public class ControlView extends VBox {

    private final ControlController store = ControlController.getInstance();

    public ControlView() {

        Label arriveLabel = new Label("Arrive per");
        Spinner<Integer> arriveSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1200, 5));
        arriveLabel.setLabelFor(arriveSpinner);
        arriveSpinner.setEditable(true);
        arriveSpinner.disableProperty().bind(store.startProperty());
        store.arrivePerNSecondProperty().bind(arriveSpinner.valueProperty());
        Label secondsLabel = new Label("seconds");
        secondsLabel.setLabelFor(arriveSpinner);
        HBox arriveRow = new HBox(arriveLabel, arriveSpinner, secondsLabel);
        arriveRow.setAlignment(Pos.CENTER);

        Label exitLabel = new Label("Exit per");
        Spinner<Integer> exitSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1200, 5));
        exitLabel.setLabelFor(exitSpinner);
        exitSpinner.setEditable(true);
        exitSpinner.disableProperty().bind(store.startProperty());
        store.exitPerNSecondProperty().bind(exitSpinner.valueProperty());
        secondsLabel = new Label("seconds");
        secondsLabel.setLabelFor(exitSpinner);
        HBox exitRow = new HBox(exitLabel, exitSpinner, secondsLabel);
        exitRow.setAlignment(Pos.CENTER);

        Button startButton = new Button();
        startButton.textProperty().bind(Bindings.when(store.startProperty()).then("Stop").otherwise("Start"));
        startButton.setOnAction(e -> store.toggleStart());

        Button openDoorButton = new Button();
        openDoorButton.textProperty().bind(Bindings.when(store.doorOpenedProperty()).then("Close the door").otherwise("Open the door"));
        openDoorButton.setOnAction(e -> store.toggleDoor());
        openDoorButton.disableProperty().bind(store.cannotCloseDoorProperty());

        startButton.prefWidthProperty().bind(arriveRow.widthProperty());
        openDoorButton.prefWidthProperty().bind(arriveRow.widthProperty());

        getChildren().addAll(arriveRow, exitRow, startButton, openDoorButton);
        setPadding(new Insets(16));
        setSpacing(32);
    }
}
