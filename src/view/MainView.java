package view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MainView extends BorderPane {
    public MainView() {
        setLeft(new WaitingStudentsListView());
        setCenter(new ControlView());
        setRight(new EnteredStudentsListView());
    }
}
