package ru.java.project.schedule;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        System.out.println(Path.of("src", "resources", "save.csv").toFile());
    }
}
