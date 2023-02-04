package ru.java.project.schedule.managers;

import ru.java.project.schedule.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
