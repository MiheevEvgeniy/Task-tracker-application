package ru.java.project.schedule.managers;

import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.util.List;

public interface TaskManager {
    //-------------ru.java.project.schedule.tasks--------------------
    List<Task> getAllTasks();

    void deleteTasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task updatedTask);

    void deleteTaskById(int id);

    //---------------epics----------------


    List<Epic> getAllEpics();

    void deleteEpics();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic updatedEpic);

    void deleteEpicById(int id);


    //subtasks
    List<Subtask> getAllSubtasks();

    void deleteSubtasks();

    Subtask getSubtaskById(int id);

    List<Subtask> getSubtasksByEpic(int epicId);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask updatedSubtask);

    void deleteSubtaskById(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
