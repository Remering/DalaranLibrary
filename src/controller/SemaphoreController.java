package controller;

import java.util.concurrent.Semaphore;

public class SemaphoreController {

    private static final SemaphoreController INSTANCE = new SemaphoreController();

    private final Semaphore enteredStudentSemaphore = new Semaphore(20);



    public static SemaphoreController getInstance() {
        return INSTANCE;
    }

    private SemaphoreController() {
    }

    public Semaphore getEnteredStudentSemaphore() {
        return enteredStudentSemaphore;
    }

}
