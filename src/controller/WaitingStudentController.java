package controller;

import model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.*;

public class WaitingStudentController {

    private static final WaitingStudentController INSTANCE = new WaitingStudentController();

    private final NamePoolController namePoolController = NamePoolController.getInstance();

    private static int nextId = 0;

    private WaitingStudentController() {

    }

    public static WaitingStudentController getInstance() {
        return INSTANCE;
    }

    private final ObservableSet<Student> waitingStudents = FXCollections.observableSet(new TreeSet<>());

    public boolean removeStudent(Student student) {
        namePoolController.returnName(student.getName());
        return waitingStudents.remove(student);
    }

    public Student removeFirstStudent() {
        Iterator<Student> iterator = waitingStudents.iterator();
        if (!iterator.hasNext()) return null;
        Student student = iterator.next();
        iterator.remove();
        return student;
    }

    public boolean addStudent() {
        Student student = new Student(nextId++, namePoolController.borrowNextName());
        return waitingStudents.add(student);
    }

    public ObservableSet<Student> getWaitingStudents() {
        return waitingStudents;
    }

}
