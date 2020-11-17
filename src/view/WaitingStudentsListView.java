package view;

import model.Student;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import controller.WaitingStudentController;

public class WaitingStudentsListView extends VBox {

    private final WaitingStudentController store = WaitingStudentController.getInstance();

    public WaitingStudentsListView() {

        ListView<Student> listView = new ListView<>();
        listView.setPlaceholder(new Label("No waiting students"));
        listView.setCellFactory(view -> {

            ListCell<Student> cell = new ListCell<Student>() {
                @Override
                protected void updateItem(Student item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) setText(null);
                    else setText(item.getName());
                }
            };
            cell.setContextMenu(generateContextMenu(cell));
            return cell;
        });
        listView.itemsProperty().bind(Bindings.createObjectBinding(() -> FXCollections.observableArrayList(store.getWaitingStudents()), store.getWaitingStudents()));

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> store.addStudent());
        addButton.prefWidthProperty().bind(listView.widthProperty());
        getChildren().addAll(addButton, listView);

    }

    private ContextMenu generateContextMenu(ListCell<Student> cell) {
        MenuItem item = new MenuItem("remove");
        item.setOnAction(e -> {
            store.removeStudent(cell.getItem());
            cell.getListView().refresh();
            System.out.println(cell.getListView().getItems());
            cell.setContextMenu(null);
        });
        return new ContextMenu(item);
    }

}
