package ru.java.project.schedule.managers;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager(8078);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
