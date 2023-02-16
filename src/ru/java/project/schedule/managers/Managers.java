package ru.java.project.schedule.managers;

import java.io.File;
import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() {
        try {
            return new HttpTaskManager("http://localhost:8078/","1");
        }catch (IOException|InterruptedException e){
            System.out.println(e);
            return null;
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
