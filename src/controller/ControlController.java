package controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import model.Student;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class ControlController {


    private static class ArriveTimerTask extends TimerTask {
        private final WaitingStudentController waitingStudentController = WaitingStudentController.getInstance();

        @Override
        public void run() {
            Platform.runLater(() -> waitingStudentController.addStudent());
        }
    }

    private static class ExitTimerTask extends TimerTask {
        private final EnteredStudentController enteredStudentController = EnteredStudentController.getInstance();
        private final Semaphore operationSemaphore = enteredStudentController.getOperationSemaphore();
        private final Semaphore enteredStudentSemaphore = SemaphoreController.getInstance().getEnteredStudentSemaphore();

        @Override
        public void run() {
            operationSemaphore.acquireUninterruptibly();
            enteredStudentSemaphore.release();
            Platform.runLater(() -> {
                enteredStudentController.removeFirstStudent();
                operationSemaphore.release();
            });
        }
    }

    private class TransferThread extends Thread {
        private final Semaphore enteredStudentSemaphore = SemaphoreController.getInstance().getEnteredStudentSemaphore();
        private final EnteredStudentController enteredStudentController = EnteredStudentController.getInstance();
        private final WaitingStudentController waitingStudentController = WaitingStudentController.getInstance();
        private final Semaphore operationSemaphore = enteredStudentController.getOperationSemaphore();

        public TransferThread() {
            setName("Transfer");
            setDaemon(true);
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                if (!isDoorOpened()) continue;
                enteredStudentSemaphore.acquireUninterruptibly();
                operationSemaphore.acquireUninterruptibly();
                Platform.runLater(() -> {
                    Student student = waitingStudentController.removeFirstStudent();
                    if (student != null) {
                        enteredStudentController.addStudent(student);
                    }
                    operationSemaphore.release();
                });
            }
        }
    }

    private static final ControlController INSTANCE = new ControlController();

    private ControlController() {
        resetTransferThread();
    }

    public static ControlController getInstance() {
        return INSTANCE;
    }

    private final IntegerProperty arrivePerNSecond = new SimpleIntegerProperty();
    private final IntegerProperty exitPerNSecond = new SimpleIntegerProperty();
    private final BooleanProperty start = new SimpleBooleanProperty(false);
    private final BooleanProperty doorOpened = new SimpleBooleanProperty(false);
    private final EnteredStudentController enteredStudentController = EnteredStudentController.getInstance();
    private BooleanProperty cannotCloseDoor = new SimpleBooleanProperty(false);
    private Timer timer;
    private Thread transferThread;

    private Thread resetTransferThread() {
        return transferThread = new TransferThread();
    }

    private Timer resetTimer() {
        return timer = new Timer(true);
    }

    public int getArrivePerNSecond() {
        return arrivePerNSecond.get();
    }

    public IntegerProperty arrivePerNSecondProperty() {
        return arrivePerNSecond;
    }

    public int getExitPerNSecond() {
        return exitPerNSecond.get();
    }

    public IntegerProperty exitPerNSecondProperty() {
        return exitPerNSecond;
    }

    public boolean isStart() {
        return start.get();
    }

    public BooleanProperty startProperty() {
        return start;
    }

    public void start() {
        Timer timer = resetTimer();
        System.out.println("arrive " + arrivePerNSecond.get());
        System.out.println("exit " + exitPerNSecond.get());
        long time = arrivePerNSecond.get() * 1000;
        timer.scheduleAtFixedRate(new ArriveTimerTask(), time, time);
        time = exitPerNSecondProperty().get() * 1000;
        timer.scheduleAtFixedRate(new ExitTimerTask(), time, time);
        resetTransferThread().start();
        ObservableSet<Student> students = enteredStudentController.getEnteredStudents();
        cannotCloseDoor.bind(Bindings.createBooleanBinding(() -> !students.isEmpty(), students));
    }

    public void stop() {
        timer.cancel();
        transferThread.interrupt();
        cannotCloseDoor.unbind();
        cannotCloseDoor.set(false);
        doorOpened.set(false);
    }

    public void toggleStart() {
        if (start.get()) {
            stop();
        } else {
            start();
        }

        start.set(!start.get());
    }


    public boolean isDoorOpened() {
        return doorOpened.get();
    }

    public BooleanProperty cannotCloseDoorProperty() {
        return cannotCloseDoor;
    }

    public BooleanProperty doorOpenedProperty() {
        return doorOpened;
    }

    public void openDoor() {
        doorOpened.set(true);
    }

    public void closeDoor() {
        doorOpened.set(false);
    }

    public void toggleDoor() {
        if (doorOpened.get()) {
            closeDoor();
        } else {
            openDoor();
        }
    }
}


