package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.ObservableList;
import model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.*;
import java.util.concurrent.Semaphore;

public class EnteredStudentController {

    private static final EnteredStudentController INSTANCE = new EnteredStudentController();

    private EnteredStudentController() {

    }

    public static EnteredStudentController getInstance() {
        return INSTANCE;
    }

    private final ObservableSet<Student> enteredStudents = FXCollections.observableSet(new TreeSet<>());

    private final NamePoolController namePoolController = NamePoolController.getInstance();

    private final Semaphore operationSemaphore = new Semaphore(1);

    public Semaphore getOperationSemaphore() {
        return operationSemaphore;
    }

    public void removeFirstStudent() {
        Iterator<Student> iterator = enteredStudents.iterator();
        if (!iterator.hasNext()) return;
        Student student = iterator.next();
        namePoolController.returnName(student.getName());
        iterator.remove();
    }

    public boolean addStudent(Student student) {
        return enteredStudents.add(student);
    }

    public ObservableSet<Student> getEnteredStudents() {
        return enteredStudents;
    }

    public ObjectBinding<ObservableList<Student>> enteredStudentsBinding() {
        return Bindings.createObjectBinding(() -> FXCollections.observableArrayList(enteredStudents), enteredStudents);
    }
}
