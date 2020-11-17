package controller;

import java.util.*;

public class NamePoolController {

    private final List<String> originNamePool;
    private final List<String> namePool = new ArrayList<>();
    private int namePoolTurn;

    private static final NamePoolController INSTANCE = new NamePoolController();

    static NamePoolController getInstance() {
        return INSTANCE;
    }

    private NamePoolController() {
        List<String> namePool = new ArrayList<>();
        try (Scanner scanner = new Scanner(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("student_names.txt")))){
            while (scanner.hasNextLine()) {
                namePool.add(scanner.nextLine());
            }
        }
        originNamePool = Collections.unmodifiableList(namePool);
        fillNamePool();

    }
    private void fillNamePool() {
        if (namePoolTurn == 0) {
            namePool.addAll(originNamePool);
            return;
        }
        namePoolTurn++;
        for (String name: originNamePool) {
            namePool.add(String.format("%s(turn %d)", name, namePoolTurn));
        }
    }

    public String borrowNextName() {
        if (namePool.isEmpty()) {
            fillNamePool();
        }
        int index = (int) Math.floor(Math.random() * namePool.size());
        return namePool.remove(index);
    }

    public void returnName(String name) {
        namePool.add(name);
    }
}
