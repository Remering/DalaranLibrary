package view;

import model.Student;
import javafx.scene.control.*;
import controller.EnteredStudentController;

public class EnteredStudentsListView extends ListView<Student> {

    public EnteredStudentsListView() {

        setPlaceholder(new Label("No entered students"));
        itemsProperty().bind(EnteredStudentController.getInstance().enteredStudentsBinding());
    }

}
