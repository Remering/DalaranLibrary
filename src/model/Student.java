package model;

import java.util.Objects;

public class Student implements Comparable<Student> {
    private final int id;
    private final String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Student o) {
        if (Objects.equals(name, o.name)) return 0;
        return id - o.id;
    }
}
